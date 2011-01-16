package web.url;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 11-1-15 ионГ1:00
 */
public class Param {
    private String op;
    private String env;


    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAllParam() {
        StringBuilder sb = new StringBuilder();
        sb.append("?").append("op=").append(op)
                .append("&env=").append(env);
        return sb.toString();
    }
}
