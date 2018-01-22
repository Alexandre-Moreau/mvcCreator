import java.util.ArrayList;
import java.util.Iterator;

public class MvcController extends MvcTopObject {
    private String name;
    private ArrayList<String[]> functions = new ArrayList<String[]>(); // name, type (render), viewBase (form, table, empty, none)
    private MvcObject object = null;

    public MvcController(String name) {
        this.name = name;
    }

    public MvcController(String name, ArrayList<String[]> functions) {
        this.name = name;
        this.functions = functions;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<String[]> getFunctions() {
        return functions;
    }

    public void setFunctions(ArrayList<String[]> functions) {
        this.functions = functions;
    }

    public void addFunction(String[] function){
        this.functions.add(function);
    }

    public MvcObject getObject() {
        return object;
    }

    public void setObject(MvcObject object) {
        this.object = object;
    }

    public String toString(){
        String content = "";
        content += "class " + this.name + "Controller extends Controller{\n";
        for (String[] function: this.functions){
            content += "\tpublic function " + function[0] + "(){\n";
            if (function[0] == "create"){
                content += "\t\t$data=[];\n";
                content += "\t\tif(!isset($_POST['nom'])){\n";
                content += "\t\t\t$this->render(\"formCreate\",$data);\n";
                content += "\t\t}else{\n";
                content += "\t\t\t$data['erreursSaisie']=[];\n";
                content += "\t\t\tforeach($_POST as $key=>$value){\n";
                content += "\t\t\t\tif($value==''){\n";
                content += "\t\t\t\t\tarray_push($data['erreursSaisie'],\"Le champ \\\"\".$key.\"\\\" est obligatoire\");\n";
                content += "\t\t\t\t}\n";
                content += "\t\t\t}\n";
                content += "\t\t\tif($data['erreursSaisie']!=[]){\n";
                content += "\t\t\t\t$this->render(\"formCreate\",$data);\n";
                content += "\t\t\t}else{\n";
                content += "\t\t\t\t$new" + this.object.getName() + " = new " + this.object.getName() + "(";
                for (Iterator<String[]> it = this.object.getAttributes().iterator(); it.hasNext(); ) {
                    String[] attribute = it.next();
                    content += "$_POST['" + attribute[0] + "']";
                    if(it.hasNext()){
                        content += ", ";
                    }
                }
                content += ");\n";
                content += "\t\t\t\t" + this.object.getName() + "::insert($new" + this.object.getName() + ");\n";
                content += "\t\t\t\t//print_r($new" + this.object.getName() + ");\n";
                content += "\t\t\t\theader('Location: ./?r=" + this.object.getName() + "/showById&id='.$new" + this.object.getName() + "->id);\n";
                content += "\t\t\t}\n";
                content += "\t\t}\n";
            }
            if(function[1] == "render"){
                switch (function[2]){
                    case "form":
                        content += "\t\t$this->render(\"form" + function[0] + "\");\n";
                        break;
                    case "table":
                        content += "\t\t$this->render(\"table" + function[0] + "\");\n";
                        break;
                    case "empty":
                        content += "\t\t$this->render(\"" + function[0] + "\");\n";
                        break;
                    case "none":
                        break;
                }
            }
            content += "\t}\n";
        }
        content += "}";
        return content;
    }

}
