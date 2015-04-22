package dmm;


public class MyRandom {
	private static volatile long next_random = System.nanoTime();
	public static float nextFloat() {
		next_random = (next_random * 25214903917L + 11);
		return (float)((next_random & 0xFFFF) / (float)65536);
    }
	public static double nextDouble(){
		next_random = (next_random * 25214903917L + 11);
		return (float)((next_random & 0xFFFFFFF) / (double)268435456);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		long a = next_random*2;
		int more = 0;
		int total  = 999999;
		for(int i=1;i<total;++i){
			double d = nextDouble();
			if(d<0.10 && d>0.05)more++;
		}
		System.out.println(more/(float)total);
//		System.out.println(0xFFFFFFF);
//		System.out.println( System.nanoTime());
	}

}
