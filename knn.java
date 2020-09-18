import java.util.*;
import java.io.File;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class knn  {
	double[][] data=new double[155][10];
	double[][] test_data=new double[59][10];
	int t=0,f=0;
	String[] Columns={"RI","Na","Mg","Al","Si","K","Ca","Ba","Fe","Type"};
	public static void main(String[] args) {
		knn kn=new knn();
		kn.getData("C://Users//vineeth//Downloads//Compressed//738_1370_bundle_archive//glass.csv");
		kn.getTestData("C://Users//vineeth//Downloads//Compressed//738_1370_bundle_archive//glass_test.csv");
		//kn.Normalize();
		kn.CoRelations();
		kn.StartKnnClassification();
	}
	public void StartKnnClassification(){
		for(double[] test:test_data)
			{  
				KnnClassify(test,9);
			}
			System.out.println("Accuracy : "+(t*100/(t+f)));
	}
	public void NewNormalize(){
		for (int i=0;i<155 ;i++ ) {

			for(int j=0;j<9;j++){
				data[i][j]=data[i][j]*50;	
			}
		}
	}
	public double[] NewNormalize(double[] test){
		for(int j=0;j<9;j++){
				test[j]=test[j]*5;
			}
			return test;
	}
	public double[] Normalize(double[] test){
		for(int k=0;k<9;k++){

				double min=getMin(k);
				double max=getMax(k);
				if(min==max){
					continue;
				}
				int fac=100;
				for(int i=0;i<155;i++){
					if(k==5)
						fac=1000;
					data[i][k]=fac*(data[i][k]-min)/(max-min);
				}
				test[k]=fac*(test[k]-min)/(max-min);
			}
			print(1,1);
			return test;
	}
	public void Normalize(){
		for(int k=0;k<9;k++){

				double min=getMin(k);
				double max=getMax(k);
				if(min==max){
					continue;
				}
				
				for(int i=0;i<155;i++){
					data[i][k]=20*(data[i][k]-min)/(max-min);
				}
				
			}
		}
			
			
	
	public void getData(String path){
		try{
			List<String> allLines=Files.readAllLines(Paths.get(path));
				int row=-1;
				for(String i:allLines){
					if(row!=-1){
						String[] temp=new String[10];
						temp=i.split(",");
						for(int j=0;j<10;j++)
							data[row][j]=Double.parseDouble(temp[j]);
						}
					row++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

	}
	public void getTestData(String path){
		try{
			List<String> allLines=Files.readAllLines(Paths.get(path));
				int row=0;
				for(String i:allLines){
					
						String[] temp=new String[10];
						temp=i.split(",");
						for(int j=0;j<10;j++)
							test_data[row][j]=Double.parseDouble(temp[j]);
						
					row++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	public void print(int n){
		
		for(int i=0;i<n;i++){
			for(int j=0;j<10;j++){
				System.out.print(data[i][j]+" ");
			}
			System.out.println();
		}
	}

	public double getMin(int col){
		double min=0;
		for(int i=0;i<155;i++){
			min=Math.min(min,data[i][col]);

		}
		return min;
	}
	public double getMax(int col){
		double max=0;
		for(int i=0;i<155;i++){
			max=Math.max(max,data[i][col]);

		}
		return max;
	}
	public void CoRelations(){
		double mean_y=0;
		for(int i=0;i<155;i++){
			mean_y=mean_y+data[i][9];
		}
		mean_y=mean_y/155;
		for(int col=0;col<9;col++){
			double pc=0;
			double num=0,den=0;
			double mean_x=0;

			for(int i=0;i<155;i++){
				mean_x=mean_x+data[i][col];
			}
			double xterm=0,yterm=0;
			mean_x=mean_x/155;
			for(int i=0;i<155;i++){
				num=num+((data[i][col]-mean_x)*(data[i][9]-mean_y));
				xterm=xterm+Math.pow((data[i][col]-(mean_x*data[i][col])),2);
				yterm=yterm+Math.pow(data[i][9]-(mean_y*data[i][9]),2);
			}
			den=Math.sqrt(xterm*yterm);
			pc=num/den;
			System.out.println("Co Relation of "+Columns[col]+"-> "+pc);
		}
	}
	public void KnnClassify(double[] test,int K){
		double min_dis=100000;
		double[] outputs=new double[155];
		HashMap<Double,Double> map=new HashMap<>();
		for(int i=0;i<155;i++){
			double curr=getDistance(test,data[i]);
			outputs[i]=curr;
			map.put(curr,data[i][9]);

		}
		double[] answers=new double[K];
		Arrays.sort(outputs);
		for(int i=0;i<K;i++){
			answers[i]=map.get(outputs[i]);
		}
		int knn_answer=getMode(answers);
		boolean flag=false;
		if(knn_answer==(int)test[9]){
			flag=true;
			t++;
		}else
			f++;
		//System.out.println(knn_answer+"   "+flag);
	}
	public void KnnClassify(double[] test){
		double min_dis=100000;
		double cls=0;
		for(int i=0;i<155;i++){
			double curr=getDistance(test,data[i]);
			if(min_dis>=curr){
				min_dis=curr;
				cls=data[i][9];
				
			}
		}
		boolean flag=false;
		if (cls==test[9]) {
			t++;
			flag=true;
		}
		else
			f++;
		//System.out.println(cls+" "+flag);
	}
	public double getDistance(double[] a,double[] b){
		double dis=0;
		for(int i=0;i<9;i++){
			dis=dis+Math.pow(a[i]-b[i],2);
		}
		return dis;
	}
	public int getMode(double[] arr){
		int max=0;
		int ans=0;
		HashMap<Double,Integer> map=new HashMap<>();
		for(double i:arr){
			if (map.containsKey(i)){
				int val=map.get(i);
				map.put(i,val+1);
				if(val+1>=max){
					max=val+1;
					ans=(int)i;
				}
			}else{
				map.put(i,1);
			}
		}
		return ans;
	}
	public void print(int s,int e){
		for(int i=s;i<(s==e? e+1 : e);i++){
			for(int j=0;j<9;j++){
				System.out.print(data[i][j]+" ");
			}
			System.out.println();
		}
	}
}