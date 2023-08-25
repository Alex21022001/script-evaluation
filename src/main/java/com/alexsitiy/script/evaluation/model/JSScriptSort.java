package com.alexsitiy.script.evaluation.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  The class that is used for holding values for script sorting.
 *  The instance of this object is created by {@link com.alexsitiy.script.evaluation.config.JSScriptSortHandlerMethod}.
 *
 *
 * @see com.alexsitiy.script.evaluation.repository.JSScriptRepository
 * */
public class JSScriptSort {
   private final List<String> sorts = new ArrayList<>();

   public void addAll(List<String> sorts){
       this.sorts.addAll(sorts);
   }

    public List<String> getSorts() {
        return sorts;
    }


}
