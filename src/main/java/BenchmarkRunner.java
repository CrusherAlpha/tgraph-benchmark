import config.Env;
import config.Model;
import config.Requests;
import graph.Neo4jExecutor;
import request.RequestExecutor;
import sql.PostgresExecutor;

import java.util.logging.Logger;

import static common.SystemInfo.systemInfo;

public class BenchmarkRunner {
    private final static Logger log = Logger.getLogger(BenchmarkRunner.class.getName());
    private static RequestExecutor getExecutor(Model model) {
        switch (model) {
            case RELATION:
                return new PostgresExecutor();
            case GRAPH:
                return new Neo4jExecutor();
            case TGRAPH:
                log.severe("UNSUPPORTED MODEL.");
                System.exit(-1);
            default:
                log.severe("UNKNOWN MODEL.");
                System.exit(-1);
        }
        return null;
    }
    public static void main(String[] args) {
        systemInfo();
        var env = Env.load("env.yaml");
        var requests = Requests.load("requests.yaml");
        var executor = getExecutor(env.Model());
        // TODO(crusher): finish the logic.
        for (var req : requests.getRequests()) {
            executor.execute(req);
        }
    }
}
