import java.util.ArrayList;

public class MvcController extends MvcTopObject {
    private String name;
    private ArrayList<String[]> functions = new ArrayList<String[]>(); // name, type (render), viewBase (form, empty, none)

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

    public String toString(){
        String content = "";
        content += "class " + this.name + "MvcController extends MvcController{\n";
        for (String[] function: this.functions){
            content += "\tpublic function " + function[0] + "(){\n";
            if(function[1] == "render"){
                switch (function[2]){
                    case "form":
                        content += "\t\t$this->render(\"form" + function[0] + "\");\n";
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
