public class patch {
    /**>FUNCTION */
   static public inject(java.lang.String $1){
        if ($1.endsWith("minecraft.jar")){
            StackTraceElement[] elements =  (new Error()).getStackTrace();
            for (int i =0;i<elements.length;i++){
                StackTraceElement item = elements[i];
                if (item.getClassName().startsWith("net/minecraft")){
                    $1 =  item.getFileName();
                    break;
                }   
            }
        }
        else if ($1.indexOf(".minecraft")>0){
            System.out.println("Rerouting: "+$1);  
            $1 = "${base}"+ $1.substring($1.indexOf(".minecraft") +".minecraft".length()); 
        }
   }
}
