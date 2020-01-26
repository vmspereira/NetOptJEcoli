/**
* Copyright 2009,
* CCTC - Computer Science and Technology Center
* IBB-CEB - Institute for Biotechnology and  Bioengineering - Centre of Biological Engineering
* University of Minho
*
* This is free software: you can redistribute it and/or modify
* it under the terms of the GNU Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Public License for more details.
*
* You should have received a copy of the GNU Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
* 
* Created inside the SysBio Research Group <http://sysbio.di.uminho.pt/>
* University of Minho
*/
package jecoli.algorithm;

// TODO: Auto-generated Javadoc
/**
 * The Enum AlgorithmTypeEnum.
 */
public enum AlgorithmTypeEnum {

	// Evolutionary Algorithm
	/** The EA. */
	EA{	
		public String getName(){
			return "Evolutionary Algorithm";
		}
		
		public String getShortName(){
			return "EA";
		}
	},
	// Simulated Annealing
	/** The SA. */
	SA{ 
		public String getName(){
			return "Simulated Annealing";
		}
		
		public String getShortName(){
			return "SA";
		}
	},
	// Differential Evolution
	/** The DE. */
	DE{	
		public String getName(){
			return "Differential Evolution";
		}
		
		public String getShortName(){
			return "DE";
		}
	},
	// Random Search
	/** The RANDOM. */
	RANDOM{	
		public String getName(){
			return "Random Search";
		}
		
		public String getShortName(){
			return "RANDOM";
		}
	},
	// Strengh Pareto Evolutionary Algorithm 2 (Multiobjective)
	/** The SPEA2. */
	SPEA2{	
		public String getName(){
			return "Strengh Pareto Evolutionary Algorithm 2 (SPEA2)";
		} 	
		
		public String getShortName(){
			return "SPEA2";
		}
		
		public boolean isMultiObjective(){
			return true;
		}
	},
	// Non-Dominated Sorting Genetic Algorithm II (Multiobjective)
	/** The NSGAII. */
	NSGAII{
		public String getName(){
			return "Non-Dominated Sorting Genetic Algorithm II (NSGAII)";
		}
		
		public String getShortName(){
			return "NSGAII";
		}
		
		public boolean isMultiObjective(){
			return true;
		}
	},
	// Cellular automata GA
	/** The CAGA. */
	CAGA{
		public String getName(){
			return "Cellular Automata Genetic Algorithm";
		}	
		
		public String getShortName(){
			return "CAGA";
		}
	};
	
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName(){
		return "Optimization Algorithm";
	}
	
	/**
	 * Gets the short name
	 * 
	 * @return the short name
	 */
	public String getShortName(){
		return "";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		return getName();
	}
	
	public boolean isMultiObjective(){
		return false;
	}
}
