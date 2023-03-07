package za.net.hanro50;

public class Patches {
    final String[] variables;
    final String body;

    public Patches(String code) {
        String header = code.substring(code.indexOf("static public inject(") + "static public inject(".length(),
                code.indexOf(")"));
        String body = code.substring(code.indexOf("{"), code.lastIndexOf("}"));

        this.variables = header.contains(",") ? header.split(",") : new String[] { header };
        for (int i = 0; i < this.variables.length; i++) {
            this.variables[i] = App.dotToPath(this.variables[i].split(" ")[0]);
        }
        this.body = body.trim();
        System.out.println(code);
        System.out.println("final:\n"+ String.join(":", this.variables));
        System.out.println("final:\n"+body);
    }
}
