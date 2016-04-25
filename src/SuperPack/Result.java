package SuperPack;

public class Result {
	public double comunication_num = 0;// 通信回数
	public double count = 0;// ステップ数
	public double distance = 0;// 移動距離
	public int num; // 試行回数
	public double density;

	public Result() {
		comunication_num = 0;
		count = 0;
		distance = 0;
		num = 0;
		density = 0;
	}

	/**
	 * 結果の初期化 すべてのパラメータを０にする
	 */
	public void Reset() {
		comunication_num = 0;
		count = 0;
		distance = 0;
		num = 0;
		density = 0;
	}

	public Result(double comunication_num, double count, double distance, double density) {
		this.comunication_num = comunication_num;
		this.count = count;
		this.distance = distance;
		this.density = density;
	}

	/**
	 * 結果の文字列化 例："500,600,1000"
	 *
	 * @param name
	 *            シミュレータの名前
	 * @return　文字列の表示
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
