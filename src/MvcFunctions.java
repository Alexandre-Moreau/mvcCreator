public abstract class MvcFunctions {

    /*
        Passer la classe en classique, les fonctions en nom statique -> avoir le choix entre des int id et des string id
     */

    public static String construct(MvcObject object) {
        String content = "\tpublic function __construct(";
        for (String[] attribute: object.getAttributes()) {
            if(object.isFk(attribute)){
                content += "$p" + Utils.firstUpper(object.fkShortName(attribute[0])) + ", ";
            }else{
                content += "$p" + Utils.firstUpper(attribute[0]) + ", ";
            }
        }
        content += "$pId = null){\n";
        content += "\t\t$this->id = $pId;\n";
        for (String[] attribute: object.getAttributes()) {
            if(object.isFk(attribute)){
                content += "\t\t$this->" + object.fkShortName(attribute[0]) + " = $p" + Utils.firstUpper(object.fkShortName(attribute[0])) + ";\n";
            }else{
                content += "\t\t$this->" + attribute[0] + " = $p" + Utils.firstUpper(attribute[0]) + ";\n";
            }
        }
        content += "\t}\n";
        return content;
    }

    public static String findById(MvcObject object) {
        String content = "\tstatic public function findById($pId){\n";
        content += "\t\t$query = db()->prepare(\"SELECT * FROM \".self::$tableName.\" WHERE id = \".$pId.\"\");\n";
        content += "\t\t$query->execute();\n";
        content += "\t\tif ($query->rowCount() > 0){\n";
        content += "\t\t\t$row = $query->fetch(PDO::FETCH_ASSOC);\n";
        content += "\t\t\t$id = $row['id'];\n";
        for (String[] attribute: object.getAttributes()) {
            if(object.isFk(attribute)) {
                content += "\t\t\t$" + object.fkShortName(attribute[0]) + " = " + Utils.firstUpper(object.fkShortName(attribute[0])) + "::FindById($row['" + attribute[0] + "']);\n";
            }else{
                content += "\t\t\t$" + attribute[0] + " = $row['" + attribute[0] + "'];\n";
            }
        }
        content += "\t\t\treturn new " + object.getName() + "(";
        for (String[] attribute: object.getAttributes()) {
            if(object.isFk(attribute)){
                content += "$" + object.fkShortName(attribute[0]) + ", ";
            }else{
                content += "$" + attribute[0] + ", ";
            }
        }
        content += "$id);\n";
        content += "\t\t}\n";
        content += "\t\treturn null;\n";
        content += "\t}\n";
        return content;
    }

    public static String findAll(MvcObject object) {
        String content = "\tstatic public function findAll($pId){\n";
        content += "\t\t$query = db()->prepare(\"SELECT id FROM \".self::$tableName);\n";
        content += "\t\t$query->execute();\n";
        content += "\t\t$returnList = array();\n";
        content += "\t\tif ($query->rowCount() > 0){\n";
        content += "\t\t\t$results = $query->fetchAll();\n";
        content += "\t\t\tforeach ($results as $row) {\n";
        content += "\t\t\t\tarray_push($returnList, self::FindById($row[\"id\"]));\n";
        content += "\t\t\t}\n";
        content += "\t\t}\n";
        content += "\t\treturn $returnList;\n";
        content += "\t}\n";
        return content;
    }

    public static String insert(MvcObject object) {
        String name = object.getName().substring(0, 1).toLowerCase() + object.getName().substring(1);
        String content = "\tstatic public function insert($" + name + ") {\n";
        content += "";
        content += "\t\t$requete = \"INSERT INTO \".self::$tableName.\" VALUES (DEFAULT";
        for (String[] attribute: object.getAttributes()) {
            if(attribute[1] == "string"){
                content += ", '\".$" + name + "->" + attribute[0] + ".\"'";
            }else if(object.isFk(attribute)){
                content += ", \".$" + name + "->" + object.fkShortName(attribute[0]) + "->id.\"";
            }else{
                content += ", \".$" + name + "->" + attribute[0] + ".\"";
            }
        }
        content += ")\";\n";
        content += "\t\t//echo $requete;\n";
        content += "\t\t$query = db()->prepare($requete);\n";
        content += "\t\t$query->execute();\n";
        content += "\t}\n";
        return content;
    }

    public static String update(MvcObject object) {
        String name = object.getName().substring(0, 1).toLowerCase() + object.getName().substring(1);
        int i = 0;
        String content = "\tstatic public function update($" + name +"){\n";
        content += "\t\t$requete = \"UPDATE \".self::$tableName.\" SET ";
        for (String[] attribute: object.getAttributes()) {
            if(attribute[1] == "string"){
                content += attribute[0] + "='\".$" + name + "->" + attribute[0] + ".\"'";
            }else if(object.isFk(attribute)){
                content += attribute[0] + "=\".$" + name + "->" + object.fkShortName(attribute[0]) + "->id.\"";
            }else{
                content += attribute[0] + "=\".$" + name + "->" + attribute[0] + ".\"";
            }
            if(i++ != object.getAttributes().size() - 1){
                content += ", ";
            }
        }
        content += " WHERE id=\".$" + name + "->id;\n";
        content += "\t\t//echo $requete;\n";
        content += "\t\t$query = db()->prepare($requete);\n";
        content += "\t\t$query->execute();\n";
        content += "\t}\n";
        return content;
    }

    public static String delete(MvcObject object) {
        String name = object.getName().substring(0, 1).toLowerCase() + object.getName().substring(1);
        String content = "\tstatic public function delete($pId){\n";
        content += "\t\t$query = db()->prepare(\"DELETE FROM \".self::$tableName.\" WHERE id=\".$" + name + "->id);\n";
        content += "\t\t$query->execute();\n";
        content += "\t}\n";
        return content;
    }
}