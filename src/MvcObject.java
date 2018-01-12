import java.util.ArrayList;

public class MvcObject extends MvcTopObject {
    private ArrayList<String[]> attributes = new ArrayList<String[]>();

    public MvcObject(String name) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public MvcObject(String name, ArrayList<String[]> attributes) {
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String[]> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String[]> attributes) {
        this.attributes = attributes;
    }

    public void addAtribute(String[] attribute){
        this.attributes.add(attribute);
    }

    @Override
    public String toString(){
        String content = "";
        content += "class " + name + " extends Model{\n";
        content += "\n";
        content += "\tstatic public $tableName = \"" + this.name.toLowerCase() + "\";\n";
        content += "\tprivate $id;\n";
        for (String[] attribute: this.attributes) {
            content += "\tprivate $" + attribute[0] + ";\n";
        }
        content += "\n";
        content += MvcFunctions.construct(this);
        content += "\n";
        content += MvcFunctions.findById(this);
        content += "\n";
        content += MvcFunctions.findAll(this);
        content += "\n";
        content += MvcFunctions.insert(this);
        content += "\n";
        content += MvcFunctions.update(this);
        content += "\n";
        content += MvcFunctions.delete(this);
        content += "}";
        return content;
    }

    public String toStringSql(){
        String name = this.getName().substring(0, 1).toLowerCase() + this.getName().substring(1);
        int i = 0;
        String content = "";
        content += "CREATE TABLE " + name + " (\n";
        content += "\tid int,\n";
        for (String[] attribute: this.attributes) {
            String type = "";
            switch (attribute[1]){
                case "string":
                    type = "varchar(30) DEFAULT ''";
                    break;
                case "int":
                    type = "int";
                default:
                    if(attribute[1].matches("string.*")){
                        type = "varchar(" + attribute[1].substring(6) + ")  DEFAULT ''";
                    }
                    break;
            }
            content += "\t" + attribute[0] + " " + type + "";
            if(i<this.attributes.size()-1){
                content += ",";
            }
            content += "\n";
            i ++;
        }
        content += ");";
        return content;
    }
}
