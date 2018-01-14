import java.util.ArrayList;

public abstract class MvcFiles {

    public static String controller() {
        String content = "";
        content += "$data = null;\n";
        content += "$option = null;\n";
        content += "\n";
        content += "class Controller{\n";
        content += "\n";
        content += "\tpublic function __construct() {\n";
        content += "\t}\n";
        content += "\n";
        content += "\tpublic function render($view, $d=null, $option=null) {\n";
        content += "\t\tglobal $data;\n";
        content += "\t\tglobal $globOption;\n";
        content += "\n";
        content += "\t\t$controller = get_class($this);\n";
        content += "\t\t$model = substr($controller, 0,\n";
        content += "\t\tstrpos($controller, \"Controller\"));\n";
        content += "\t\t$data = $d;\n";
        content += "\t\t$globOption = $option;\n";
        content += "\t\tinclude_once \"views/\".strtolower($model).\"/\".$view.\".php\";\n";
        content += "\t}\n";
        content += "}";
        return content;
    }

    public static String db(String dbName, String user, String password){
        String content = "";
        content += "$host = \"localhost\";\n";
        content += "$databaseName = \"" + dbName + "\";\n";
        content += "$user = \"" + user + "\";\n";
        content += "$password = \"" + password + "\";\n";
        content += "\n";
        content += "$db = new PDO(\"mysql:host=\".$host.\";dbname=\".$databaseName, $user, $password);\n";
        content += "$db->exec(\"SET CHARACTER SET utf8\");\n";
        content += "\n";
        content += "function db(){\n";
        content += "\tglobal $db;\n";
        content += "\treturn $db;\n";
        content += "}";
        return content;
    }

    public static String footer(){
        String content = "";
        content += "\t\t<footer>\n";
        content += "\t\t\tfooter\n";
        content += "\t\t</footer>\n";
        content += "\t</body>\n";
        content += "\t</html>\n";
        return content;
    }

    public static String header(){
        String content = "<!DOCTYPE html>\n";
        content += "<html>\n";
        content += "\t<header>\n";
        content += "\t\t<a href=\".?r=site/index\">Accueil</a>\n";
        content += "\t</header>\n";
        content += "\t<?php\n";
        content += "\t/*\n";
        content += "\tprint(\"  --debug: code à enlever dans header.php--  \");\n";
        content += "\tprint_r($_SESSION);\n";
        content += "\t*/\n";
        content += "\t?>\n";
        content += "<body>\n";
        return content;
    }

    public static String header(ArrayList<MvcController> controllers){
        String content = "<!DOCTYPE html>\n";
        content += "<html>\n";
        content += "\t<header>\n";
        content += "\t\t<a href=\".?r=site/index\">Accueil</a>\n";
        content += "\t\t<ul>\n";
        for (MvcController ctrl: controllers) {
            for (String[] function : ctrl.getFunctions()) {
                if(function[1] == "render"){
                    if(!function[0].contains("ById")){
                        content += "\t\t\t<li><a href=\".?r=" + ctrl.getName() + "/" + function[0] + "\">" + ctrl.getName() + "/" + function[0] + "</a></li>\n";
                    }
                }
            }
        }
        content += "\t\t</ul>\n";
        content += "\t</header>\n";
        content += "\t<?php\n";
        content += "\t/*\n";
        content += "\tprint(\"  --debug: code à enlever dans header.php--  \");\n";
        content += "\tprint_r($_SESSION);\n";
        content += "\t*/\n";
        content += "\t?>\n";
        content += "<body>\n";
        return content;
    }

    public static String index(){
        String content = "";
        content += "//Debug. Désactiver en prod\n";
        content += "error_reporting(E_ALL | E_STRICT);\n";
        content += "ini_set('display_errors', true);\n";
        content += "\n";
        content += "include_once \"db.php\";\n";
        content += "include_once \"tools.php\";\n";
        content += "\n";
        content += "session_start();\n";
        content += "\n";
        content += "include_once \"views/header.php\";\n";
        content += "date_default_timezone_set('Europe/Paris');\n";
        content += "include_once \"controllers/route.php\";\n";
        content += "include_once \"views/footer.php\";\n";
        return content;
    }

    public static String model() {
        String content = "abstract class Model{\n";
        content += "\tpublic function __construct(){\n";
        content += "\t}\n";
        content += "\n";
        content += "\tpublic function __get($fieldName) {\n";
        content += "\t\t$varName = $fieldName;\n";
        content += "\t\tif (property_exists(get_class($this), $varName))\n";
        content += "\t\t\treturn $this->$varName;\n";
        content += "\t\telse\n";
        content += "\t\t\tthrow new Exception(\"Unknown attribute: \".$fieldName);\n";
        content += "\t}\n";
        content += "\n";
        content += "\tpublic function __set($fieldname,$value) {\n";
        content += "\t\t$this->$fieldname = $value;\n";
        content += "\t}\n";
        content += "}";
        return content;
    }

    public static String route(){
        String content = "";
        content += "\n";
        content += "// Accès POST ou GET indifférent\n";
        content += "$parameters = array();\n";
        content += "if (isset($_POST))\n";
        content += "\tforeach($_POST as $k=>$v)\n";
        content += "\t\t$parameters[$k] = $v;\n";
        content += "if (isset($_GET))\n";
        content += "\tforeach($_GET as $k=>$v)\n";
        content += "\t\t$parameters[$k] = $v;\n";
        content += "\n";
        content += "// Pour accès ultérieur sans \"global\"\n";
        content += "function parameters() {\n";
        content += "\tglobal $parameters;\n";
        content += "\treturn $parameters;\n";
        content += "}\n";
        content += "\n";
        content += "// Gestion de la route : paramètre r = controller/action\n";
        content += "if (isset(parameters()[\"r\"])) {\n";
        content += "\t$route = parameters()[\"r\"];\n";
        content += "\tif (strpos($route,\"/\") === FALSE)\n";
        content += "\t\tlist($controller, $action) = array($route, \"index\");\n";
        content += "\telse\n";
        content += "\t\tlist($controller, $action) = explode(\"/\", $route);\n";
        content += "\t$controller = ucfirst($controller).\"Controller\";\n";
        content += "\t$c = new $controller();\n";
        content += "\t$c->$action();\n";
        content += "} else {\n";
        content += "\t$c = new SiteController();\n";
        content += "\t$c->index();\n";
        content += "}\n";
        return content;
    }

    public static String tools(){
        String content = "";
        content += "function __autoload($name) {\n";
        content += "\t$dir = \"models\";\n";
        content += "\tif (strpos($name,\"Controller\") !== FALSE)\n";
        content += "\t\t$dir = \"controllers\";\n";
        content += "\tinclude_once $dir.\"/\".$name.\".php\";\n";
        content += "}\n";
        return content;
    }

    // -- Classic views

    public static String formView(MvcObject object){
        String content = "";
        content += "form "+ object.getName() + "\n";
        return content;
    }

    public static String tableView(MvcObject object){
        String content = "";
        content += "table "+ object.getName() + "\n";
        return content;
    }
}
