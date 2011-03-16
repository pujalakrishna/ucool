/**
 * Module Loader for Systems (eg. Detail) by yunqian & xiaofan
 */

TShop.add('loader', function(T) {

    var S = KISSY, DOM = S.DOM, Event = S.Event,
        win = window,
        log = S.log;

    T.Loader = function() {

        var modCache = {},
            assetsComboBase = '',                       // assets combo 的基础路径

            DATA_MOD_INFO   = 'data-mod-info',          // 模块信息
            DATA_MOD_CID    = 'data-mod-cid',           // (动态生成)，用于通过元素找到模块信息
            DATA_MOD_QUERY  = 'data-mod-query',         // 请求 HTML 时会附加上去的请求参数
            PATH_MODS       = 'div.J_TMod',             // 用于取得模块的 CSS 路径

            config   = {
                'pipeHost':     'tbskip.taobao.com',    // 获取HTML的主机，测试时传入
                'assetsHost':   'a.tbcdn.cn',           // assets主机，测试时传入
                'assetsPath':   '/mods/',               // 模块文件夹在 assets 服务器上的路径
                'monitorHost':  'http://igw.monitor.taobao.com/monitor-gw/itemdesc.htm', // 接收统计请求的主机
                'htmlSuffix':   '.htm',                 // 请求html的后缀
                'comboLimit':   10,                     // 单次 combo 模块的数量限制
                'preloadColumnCls': []                  // 需要预先载入前几个模块的列的Class
            };

        return {

            /**
             * 初始化 Loader
             * @param container
             * @param cfg
             */
            init: function(container, cfg) {
                DOM = S.DOM, Event = S.Event;
                
                if ( !(container = DOM.get(container)) || !cfg ) {
                    log('warn: 容器不存在或没有传入配置信息');
                    return;
                }

                S.mix(config, cfg);
                assetsComboBase = config['assetsHost'] + config['assetsPath'];
                
                var self = this, mod, delay, cols,
                    midStat = [],
                    queue = [],
                    scrollQueue = [];

                S.mix(self, S.EventTarget);

                // 遍历模块，获取模块信息并分组
                S.each(DOM.query(PATH_MODS, container), function(el) {
                    if ( !checkMod(mod = S.unparam(el.getAttribute(DATA_MOD_INFO))) ) {
                        return;
                    }

                    // mid 统计并去重
                    if (S.indexOf(mod['mid'], midStat) === -1) {
                        midStat.push(mod['mid']);
                    }

                    // 将以逗号分割的 css 和 js 路径转成数组
                    S.each(['css', 'js'], function(key) {
                        if (mod[key] && S.isString(mod[key]) && mod[key].indexOf(',') > -1) {
                            mod[key] = mod[key].split(',');
                        }
                    });

                    mod['el'] = (el.setAttribute(DATA_MOD_CID, mod.cid), el);
                    mod['cm'] = [mod['cid'], mod['mid']].join(':');

                    // 根据 cid 对模块信息进行 cache
                    modCache[mod['cid']] = mod;

                    // "预先载入"模块
                    if ( !(delay = mod['delay']) ) {
                        queue.push(mod);
                    }
                    // "立刻延迟载入"模块
                    else if (delay === '0') {
                        scrollQueue.push(mod);
                    }
                    // "条件延迟加载"模块
                    else {
                        var o = S.DataLazyload({diff:+delay});
                        o.addCallback(el, function() {
                            self.load(self.getMod(el));
                        });
                    }
                });

                // 把各列的前三个模块合并到queue里一起载入
                cols = S.makeArray(config['preloadColumnCls']);
                S.each(cols, function(col) {
                    if ( (col = DOM.get(col, container)) ) {
                        queue.push(self.getMod(DOM.children(col, 'div').slice(0, 3)));
                    }
                });

                // 处理"预先载入"模块
                self.load(queue);

                // 处理"立刻延迟载入"模块
                Event.on(win, 'scroll', function() {
                    self.load(scrollQueue);

                    // 避免重复 load
                    Event.remove(win, 'scroll', arguments.callee);
                });

                // 统计该页面用到了哪些模块
                try {
                    if (parseInt(Math.random()*100) === 0) {
                        new Image().src = [
                            config['monitorHost'],
                            '?mid=',    midStat.join(','),
                            '&appId=',  win['g_config']['appId'],
                            '&pageId=', win['g_config']['pageId']
                        ].join('');
                    }
                } catch(e) {}
                
                self.fire('init');
            },

            /**
             * 载入一个或多个模块
             * @param m {Array|Object} 模块配置信息
             */
            load: function(m) {
                var self = this, jsmod, map = {}, key;
                
                // 函数重载：多个模块时，合并或直接载入
                if (S.isArray(m)) {
                    S.each(m, function(mod) {
                        // 非 combo 模块直接载入
                        if (!mod['combo']) {
                            self.load(mod);
                        }
                        // combo 模块添加到 map 进行分组
                        else {
                            map[mod['combo']] = (map[mod['combo']] || []).concat(mod);
                        }
                    });

                    // 根据 combo base 进行模块合并，并限制每次合并的模块数
                    for (key in map) {
                        while (map[key].length > 0) {
                            self.load(mergeMod(map[key].splice(0, config['comboLimit'])));
                        }
                    }
                    
                    return;
                }

                // 函数重载：单个模块(可能是合并后的模块)，直接载入
                changeMod(m, {'status': 'loading'});

                // 载入 js
                if (m['js'] && m['js'].length) {
                    T.add((jsmod  = buildJSModName(m['js'])), {
                        fullpath: comboAssets(m['js'])
                    }).use(jsmod);

                    // 请求 html 时附上 jsmod 参数
                    // 后端遇到 jsmod 参数则在 insert 外包一层：
                    // KISSY.use(%jsmod%, function() { /* insert html */ }) 
                    // m['jsmod'] = jsmod;
                }

                // 载入 css
                if (m['css'] && m['css'].length) S.getScript(comboAssets(m['css']));

                // 载入 html
                S.getScript(buildURL(m));
            },

            /**
             * 根据 cid 插入 HTML
             * @param data {Object} 至少包含 cid 和 content 的数据
             */
            insert: function(data) {
                var mod;

                if (S.isPlainObject(data) && data['cid'] && data['content']) {
                    mod = modCache[data['cid']];

                    // 插入HTML数据，并运行包含的 script 标签
                    DOM.html(mod['el'], data['content'], true);
                    // 修改模块状态为 loaded
                    changeMod(mod, {'status': 'loaded'});
                } else {
                    log('warn: 回调数据中未包含 cid 或 content');
                }
            },

            /**
             * 通过 cid 获取模块元素
             * @param o {String}
             */
            getModElem: function(o) {
                return (this.getMod(o) || {})['el'];
            },

            /**
             * 通过 cid 或 element 获取模块信息
             * @param o {String|Array}
             */
            getMod: function(o) {
                if (S.isArray(o)) {
                    var ret = [], mod, self = this;
                    S.each(o, function(el) {
                        if ( (mod = self.getMod(el)) ) {
                            ret.push(mod);
                        }
                    });
                    return ret;
                }
                
                return this[S.isString(o) ? '_getModByCID' : '_getModByElement'](o);
            },

            /**
             * 通过 cid 获取模块信息
             * @param cid
             */
            _getModByCID: function(cid) {
                return modCache[cid];
            },

            /**
             * 通过 element 获取模块信息
             * @param el
             */
            _getModByElement: function(el) {
                return modCache[el.getAttribute(DATA_MOD_CID)];
            }

        };

        /*************************************************************************/
        // utilities

        /**
         * 检查模块信息
         */
        function checkMod(mod) {
            if (!mod || !mod['cid'] || !mod['mid'] || !mod['html']) {
                log('模块信息必须包含 cid、mid 和 html，请检查');
                log(mod);
                return false;
            }
            return true;
        }

        /**
         * 修改模块信息，支持同时修改多个
         * @param m
         * @param props
         */
        function changeMod(m, props) {
            if (S.isArray(m)) {
                S.each(m, function(mod) {
                    changeMod(mod, props);
                });
            } else {
                S.mix(m, props);
            }
        }

        /**
         * merge mods for load
         */
        function mergeMod(mods) {
            var cm = [], css = [], js = [], el = [], cs = [], v = 0;

            S.each(mods, function(mod) {
                cm.push(mod['cm']);
                cs.push(mod['cid']);
                mod['css'] && (css = css.concat(S.makeArray(mod['css'])));
                mod['js']  && (js  = js.concat(S.makeArray(mod['js'])));
                mod['el']  && (el  = el.concat(S.makeArray(mod['el'])));
                mod['v']   && (v =  mod['v'] > v ? mod['v'] : v); // 取 v 的最大值
            });

            return {
                'cm'        : cm,
                'cid'       : cs,
                'css'       : css,
                'js'        : js,
                'el'        : el,
                'combo'     : mods[0]['combo'],
                'v'         : v
            };
        }

        /**
         * combo assets (css or js)
         * @param as {String|Array}
         */

        function comboAssets(as) {
            as = (as = S.makeArray(as)).length === 1 ? as[0] : ('??' + as.join(','));
            return assetsComboBase + as;
        }

        /**
         * build js mod name
         * @param res
         */
        function buildJSModName(res) {
            return S.makeArray(res).join(',');
        }

        /**
         * callback is not necessary
         */
        function buildURL(mod) {
            var url, copy = {}, param;

            url = config['pipeHost'];
            url += mod['combo'] || mod['html'];
            url += config['htmlSuffix'];
            url += '?';

            // 仅请求 cm 和 v，并保证参数顺序
            S.each(['cm', 'v'], function(key) {
                copy[key] = mod[key];
                // url += '&'+key+'='+mod[key];
            });
            url += S.param(copy);

            // 附加参数
            if ( (param = buildParam(mod['el'])) ) {
                url += '&' + param;
            }

            return url;
        }

        /**
         * build params for one or more element
         * @param els {HTMLElement|Array} 
         */
        function buildParam(els) {
            var props = {}, prop, key;

            S.each(S.makeArray(els), function(el) {
                if ( (prop = el.getAttribute(DATA_MOD_QUERY)) ) {
                    for (key in (prop = S.unparam(prop))) {
                        props[key] = props[key] ? S.makeArray(props[key]).concat(prop[key]) : prop[key];
                    }
                }
            });

            return S.param(props);
        }

    }();

}, { host: 'tshop-core' });
