import java.util.*;

class Matrixm{
    
    static void OnMult(int m_ar, int m_br){
        long Time1, Time2;
	
        String st;
        double temp;
        int i, j, k;
    
        double [][]pha, phb, phc;
        pha= new double[m_ar][m_br];
        phb= new double[m_ar][m_br];
        phc= new double[m_ar][m_br];
    
        for(i=0; i<m_ar; i++)
            for(j=0; j<m_ar; j++)
                pha[i][j] =1.0;
    
        for(i=0; i<m_br; i++)
            for(j=0; j<m_br; j++)
                phb[i][j] = i+1;
    
    
    
        Time1 = System.currentTimeMillis();
    
        for(i=0; i<m_ar; i++)
        {	for( j=0; j<m_br; j++)
            {	temp = 0;
                for( k=0; k<m_ar; k++)
                {	
                    temp += pha[i][k] * phb[k][j];
                }
                phc[i][j]=temp;
            }
        }
    
    
        Time2 = System.currentTimeMillis();
        st= String.format("Time: %3.3f seconds\n", (double)(Time2 - Time1)/1000);
        System.out.println(st);
        //System.out.printf("Time: %3.3f seconds\n", (double)(Time2 - Time1)/1000);
     
        // display 10 elements of the result matrix tto verify correctness
        System.out.println("Result matrix:");
        for(i=0; i<1; i++)
        {	for(j=0; j<Math.min(10,m_br); j++)
                System.out.print(phc[i][j]+" ");
        }
        System.out.println();
    }

    // add code here for line x line matriz multiplication
    static void OnMultLine(int m_ar, int m_br)
    {
        long Time1, Time2;
	
        String st;
        int i, j, k;
    
        double [][]pha, phb, phc;
        pha= new double[m_ar][m_br];
        phb= new double[m_ar][m_br];
        phc= new double[m_ar][m_br];
    
        for(i=0; i<m_ar; i++)
            for(j=0; j<m_ar; j++)
                pha[i][j] =1.0;
    
        for(i=0; i<m_br; i++)
            for(j=0; j<m_br; j++)
                phb[i][j] = i+1;
    
    
    
        Time1 = System.currentTimeMillis();
    
        for(i=0; i<m_ar; i++){
            
            for( j=0; j<m_br; j++){
                   
                for(k=0;k<m_ar;k++){

                    phc[i][k] += pha[i][j]*phb[j][k];
                }
                
            }
        }
    
    
        Time2 = System.currentTimeMillis();
        st= String.format("Time: %3.3f seconds\n", (double)(Time2 - Time1)/1000);
        System.out.println(st);
        //System.out.printf("Time: %3.3f seconds\n", (double)(Time2 - Time1)/1000);
     
        // display 10 elements of the result matrix tto verify correctness
        System.out.println("Result matrix:");
        for(i=0; i<1; i++)
        {	for(j=0; j<Math.min(10,m_br); j++)
                System.out.print(phc[i][j]+" ");
        }
        System.out.println();
    
    }

    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        int op=1;
        int lin, col;
        do{
            System.out.println("1. Multiplication");
            System.out.println("2. Line Multiplication");
            System.out.println("3. Block Multiplication");
            System.out.print("Selection: "); op=scanner.nextInt();

            System.out.println("Dimensions: lins=cols ? ");
            lin=scanner.nextInt();
            col=lin;
    
            //start counting

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