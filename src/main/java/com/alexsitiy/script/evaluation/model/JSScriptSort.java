package com.alexsitiy.script.evaluation.model;

import java.util.ArrayList;
import java.util.List;

public class JSScriptSort {
   private final List<String> sorts = new ArrayList<>();

   public void addAll(List<String> sorts){
       this.sorts.addAll(sorts);
   }

    public List<String> getSorts() {
        return sorts;
    }


}
