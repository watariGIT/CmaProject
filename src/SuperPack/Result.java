package SuperPack;

public class Result {
	private double comunication_num = 0;// 通信回数
	private double count = 0;// ステップ数
	private double distance = 0;// 移動距離
	private double density;

	public Result(double comunication_num, double count, double distance, double density) {
		this.comunication_num = comunication_num;
		this.count = count;
		this.distance = distance;
		this.density = density;
	}

	/**
	 * 結果の文字列化 例："500,600,1000"
	 *
	 * @return 文字列の表示
	 */
	public String toString() {
		String s = "";
		s += count + ",";
		s += comunication_num + ",";
		s += distance + ",";
		s += density;
		return s;
	}
}
