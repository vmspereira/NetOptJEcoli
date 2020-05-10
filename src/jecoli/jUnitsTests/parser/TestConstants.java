/* Generated By:JavaCC: Do not edit this line. TestConstants.java */
package jecoli.jUnitsTests.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface TestConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int OPERATOR = 5;
  /** RegularExpression Id. */
  int RANDOMSEED = 6;
  /** RegularExpression Id. */
  int INITIALPOP = 7;
  /** RegularExpression Id. */
  int SOLUTION = 8;
  /** RegularExpression Id. */
  int EXPECTEDSOL = 9;
  /** RegularExpression Id. */
  int SPECIFICOPERATORVALUES = 10;
  /** RegularExpression Id. */
  int LBRACKET = 11;
  /** RegularExpression Id. */
  int RBRACKET = 12;
  /** RegularExpression Id. */
  int TRUE = 13;
  /** RegularExpression Id. */
  int FALSE = 14;
  /** RegularExpression Id. */
  int HALFCOMMA = 15;
  /** RegularExpression Id. */
  int DOUBLE = 16;
  /** RegularExpression Id. */
  int DIGIT = 17;
  /** RegularExpression Id. */
  int PLUS = 18;
  /** RegularExpression Id. */
  int ADD = 19;
  /** RegularExpression Id. */
  int MINUS = 20;
  /** RegularExpression Id. */
  int SEMICOMMA = 21;
  /** RegularExpression Id. */
  int MULT = 22;
  /** RegularExpression Id. */
  int DIV = 23;
  /** RegularExpression Id. */
  int POW = 24;
  /** RegularExpression Id. */
  int RETURN = 25;
  /** RegularExpression Id. */
  int DO = 26;
  /** RegularExpression Id. */
  int END = 27;
  /** RegularExpression Id. */
  int EQUAL = 28;
  /** RegularExpression Id. */
  int SUBTRACTION = 29;
  /** RegularExpression Id. */
  int LPAR = 30;
  /** RegularExpression Id. */
  int RPAR = 31;
  /** RegularExpression Id. */
  int SIZE = 32;
  /** RegularExpression Id. */
  int ISMAX = 33;
  /** RegularExpression Id. */
  int GENNUM = 34;
  /** RegularExpression Id. */
  int ARRAY = 35;
  /** RegularExpression Id. */
  int SET = 36;
  /** RegularExpression Id. */
  int BOOLEAN = 37;
  /** RegularExpression Id. */
  int TRUEORFALSE = 38;
  /** RegularExpression Id. */
  int INTEGER = 39;
  /** RegularExpression Id. */
  int SOLUTIONTYPE = 40;
  /** RegularExpression Id. */
  int LOWERBOUND = 41;
  /** RegularExpression Id. */
  int UPPERBOUND = 42;
  /** RegularExpression Id. */
  int NUMBEROFSOLUTIONSTOSELECT = 43;
  /** RegularExpression Id. */
  int NUMBEROFSOLUTIONSPERTOURNAMENT = 44;
  /** RegularExpression Id. */
  int K = 45;
  /** RegularExpression Id. */
  int MAXSIZE = 46;
  /** RegularExpression Id. */
  int FITNESS = 47;
  /** RegularExpression Id. */
  int ILINEARREPRESENTATION = 48;
  /** RegularExpression Id. */
  int BITFLIPMUTATION = 49;
  /** RegularExpression Id. */
  int EVOLUTIONARYALGORITHM = 50;
  /** RegularExpression Id. */
  int CELLULARGENETICALGORITHM = 51;
  /** RegularExpression Id. */
  int CUTANDSPLICECROSSOVER = 52;
  /** RegularExpression Id. */
  int DIFFERENTIALEVOLUTIONALGORITHM = 53;
  /** RegularExpression Id. */
  int GAUSSIANPERTURBATIONMUTATION = 54;
  /** RegularExpression Id. */
  int INTEGERADDMUTATION = 55;
  /** RegularExpression Id. */
  int SETSHRINKMUTATION = 56;
  /** RegularExpression Id. */
  int SETGROWTHMUTATION = 57;
  /** RegularExpression Id. */
  int SETRANDOMMUTATION = 58;
  /** RegularExpression Id. */
  int LINEARGENOMEGROWMUTATION = 59;
  /** RegularExpression Id. */
  int LINEARGENOMERANDOMMUTATION = 60;
  /** RegularExpression Id. */
  int LINEARGENOMESHRINKMUTATION = 61;
  /** RegularExpression Id. */
  int NSGAIIALGORITHM = 62;
  /** RegularExpression Id. */
  int ONEPOINTCROSSOVER = 63;
  /** RegularExpression Id. */
  int TWOPOINTCROSSOVER = 64;
  /** RegularExpression Id. */
  int ROULETTEWHEELSELECTION = 65;
  /** RegularExpression Id. */
  int SIMULATEDANNEALING = 66;
  /** RegularExpression Id. */
  int SPEA2ALGORITHM = 67;
  /** RegularExpression Id. */
  int TOURNAMENTSELECTION = 68;
  /** RegularExpression Id. */
  int STRING = 69;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "\"\\n\"",
    "\"Operator\"",
    "\"RandomSeed\"",
    "\"InitialPopulation\"",
    "\"Solution\"",
    "\"ExpectedSolution\"",
    "\"SpecificOperatorValues\"",
    "\"{\"",
    "\"}\"",
    "\"true\"",
    "\"false\"",
    "\":\"",
    "<DOUBLE>",
    "<DIGIT>",
    "\"plus\"",
    "\"add\"",
    "\"minus\"",
    "\";\"",
    "\"mult\"",
    "\"div\"",
    "\"pow\"",
    "\"return\"",
    "\"do\"",
    "\"end\"",
    "\"equals\"",
    "\"subtraction\"",
    "\"(\"",
    "\")\"",
    "\"size\"",
    "\"isMaximization\"",
    "\"numberOfGenerations\"",
    "\"Array\"",
    "\"Set\"",
    "\"Boolean\"",
    "<TRUEORFALSE>",
    "\"Integer\"",
    "\"SolutionType\"",
    "\"lowerBound\"",
    "\"upperBound\"",
    "\"numberOfSolutionsToSelect\"",
    "\"numberOfSolutionsPerTournment\"",
    "\"k\"",
    "\"MaxSize\"",
    "\"fitness\"",
    "\"ILinearRepresentation\"",
    "\"BitFlipMutation\"",
    "\"EvolutionaryAlgorithm\"",
    "\"CellularGeneticAlgorithm\"",
    "\"CutAndSpliceCrossover\"",
    "\"DifferentialEvolutionAlgorithm\"",
    "\"GaussianPerturbationMutation\"",
    "\"IntegerAddMutation\"",
    "\"SetShrinkMutation\"",
    "\"SetGrowthMutation\"",
    "\"SetRandomMutation\"",
    "\"LinearGenomeGrowMutation\"",
    "\"LinearGenomeRandomMutation\"",
    "\"LinearGenomeShrinkMutation\"",
    "\"NSGAIIAlgorithm\"",
    "\"OnePointCrossover\"",
    "\"TwoPointCrossover\"",
    "\"RouletteWheelSelection\"",
    "\"SimulatedAnnealing\"",
    "\"SPEA2Algorithm\"",
    "\"TournamentSelection\"",
    "<STRING>",
  };

}
