package cn.fansunion.htmlrender;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String html = "txt.this is a line of text,txt.txt again,img.imag1,img.image2";
		HtmlRender render = new HtmlRender(html);
		render.rende();
		render.close();
	}

}
