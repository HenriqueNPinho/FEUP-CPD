
import java.util.*;

class MatrixProd{
    static void printTime(long Time1, long Time2){
        System.out.printf("Time: %3.3f seconds\n", (double)(Time2 - Time1) / 1000);
    }
    
    static void printMatrix(double phc[], int m_br){

        System.out.println("Result matrix: ");
        for(int i=0; i<1; i++)
        {	for(int j=0; j<Math.min(10,m_br); j++)
                System.out.print(phc[j]+ " ");
        }
        System.out.println();
    }
    static void OnMult(int m_ar, int m_br){
        long Time1, Time2;
        double temp;
        int i, j, k;

        double []pha, phb, phc;
        pha= new double[m_ar*m_br];
        phb= new double[m_ar*m_br];
        phc= new double[m_ar*m_br];
    
        for(i=0; i<m_ar; i++){
            for(j=0; j<m_br; j++){
                pha[i*m_ar + j] =1.0;
                phb[i*m_br + j] =i+1.0;
            }
        }
    
        Time1 = System.currentTimeMillis();
        for(i=0; i<m_ar; i++)
        {	for( j=0; j<m_br; j++)
            {	temp = 0;
                for( k=0; k<m_ar; k++)
                {	
                    temp += pha[i*m_ar+k] * phb[k*m_br+j];
                }
                phc[i*m_ar+j]=temp;
            }
        }
        Time2 = System.currentTimeMillis();

        printTime(Time1, Time2);
        printMatrix(phc, m_br);
    }

    // add code here for line x line matriz multiplication
    static void OnMultLine(int m_ar, int m_br)
    {
        long Time1, Time2;
        int i, j, k;
    
        double []pha, phb, phc;
        pha= new double[m_ar*m_br];
        phb= new double[m_ar*m_br];
        phc= new double[m_ar*m_br];
    
        for(i=0; i<m_ar; i++){
            for(j=0; j<m_br; j++){
                pha[i*m_ar + j] = 1.0;
                phb[i*m_ar + j] = i+1.0;
            }
        }
    
        Time1 = System.currentTimeMillis();
        for(i=0; i<m_ar; i++)
        {	for( j=0; j<m_br; j++)
            {	double pha_aux = pha[i*m_ar+j];
                for( k=0; k<m_ar; k++)
                {	
                    phc[i*m_ar+k] += pha_aux * phb[j*m_br+k];
                }
            }
	    }
        Time2 = System.currentTimeMillis();
        
        printTime(Time1, Time2);
        printMatrix(phc, m_br);
    }

    public static void main(String args[]){
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);
        int op=1;
        int lin, col;

        do{
            System.out.println("1. Multiplication");
            System.out.println("2. Line Multiplication");
            System.out.print("Selection: "); op=scanner.nextInt();

            System.out.print("Dimensions: lins=cols ? ");
            lin=scanner.nextInt();
            col=lin;
            
            switch(op){
                case 1: 
				    OnMult(lin, col);
				    break;
			    case 2:
				    OnMultLine(lin, col);  
				    break;
            }
        }while(op != 0);

        scanner.close();
    }
}