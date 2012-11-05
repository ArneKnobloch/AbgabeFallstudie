package marcsEisdiele.server;

import java.util.Comparator;


public class SortCriterion implements Comparator<Integer>{

		@Override
	    public int compare(Integer un1, Integer un2) {
	        return un1-un2;
	    }

}
