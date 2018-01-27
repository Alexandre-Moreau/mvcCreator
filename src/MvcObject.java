import java.util.ArrayList;
import java.util.Iterator;

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

    public String getNameCamelCase(){
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public boolean containsFk(){
        for (String[] attr: attributes) {
            if(isFk(attr)){
                return true;
            }
        }
        return false;
    }

    //Passer en abstract?
    public boolean isFk(String[] attribute){
        if(attribute[0].length() > 2 && attribute[0].substring(attribute[0].length() - 3).equals("_id")){
            return true;
        }else{
            return false;
        }
    }

    public String fkShortName(String attr){
        return(attr.substring(0, attr.length() - 3));
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String[]> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String[]> attributes) {
        for (String[] attr: attributes) {
            this.addAtribute(attr);
        }
    }

    public void addAtribute(String[] attribute){
        this.attributes.add(attribute);
    }

    public void addAtribute(MvcObject object) {
        String[] attr = new String[3];
        attr[0] = object.getNameCamelCase() + "_id";
        attr[1] = "int";
        this.attributes.add(attr);
    }

    @Override
    public String toString(){
        String content = "";
        content += "class " + name + " extends Model{\n";
        content += "\n";
        content += "\tstatic public $tableName = \"" + this.name.toLowerCase() + "\";\n";
        content += "\tprivate $id;\n";
        for (String[] attribute: this.attributes) {
            if(isFk(attribute)){
                content += "\tprivate $" + attribute[0].substring(0, attribute[0].length() - 3) + ";\n";
            }else{
                content += "\tprivate $" + attribute[0] + ";\n";
            }
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
        String content = "";
        content += "CREATE TABLE " + getNameCamelCase() + " (\n";
        content += "\tid int AUTO_INCREMENT,\n";
        for (String[] attribute: this.attributes) {
            String type = "";
            switch (attribute[1]){
                case "string":
                    if(attribute.length>2){
                        type = "varchar(" + attribute[2] + ") DEFAULT ''";
                    }else{
                        type = "varchar(30) DEFAULT ''";
                    }
                    break;
                case "int":
                    type = "int";
                    break;
                default:
                    break;
            }
            content += "\t" + attribute[0] + " " + type + ",\n";
        }
        content += "\tCONSTRAINT pk_" + this.getName().toLowerCase() + "_id PRIMARY KEY (id)\n";
        content += ");";
        return content;
    }

    public String toStringForeignKeys(){
        String content = "";
        ArrayList<String> fks_attr = new ArrayList<String>();
        content += "ALTER TABLE " + getNameCamelCase() + "\n";
        for (String[] attr: this.attributes) {
            if(isFk(attr)){
                fks_attr.add(fkShortName(attr[0]));
            }
        }
        for (Iterator<String> attrNames = fks_attr.iterator(); attrNames.hasNext(); ) {
            String attrName = attrNames.next();
            content += "ADD CONSTRAINT fk_" + getNameCamelCase() + "_" + attrName + "_id FOREIGN KEY (" + attrName + "_id) REFERENCES " + attrName + "(id)";
            if(attrNames.hasNext()){
                content += ",\n";
            }else{
                content += ";\n";
            }
        }

        return content;
    }
}
