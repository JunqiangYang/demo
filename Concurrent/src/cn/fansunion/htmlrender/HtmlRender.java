package cn.fansunion.htmlrender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HtmlRender {
	String html;
	ExecutorService exec;
	CompletionService<Image> completionService;

	public HtmlRender(String html) {
		this.html = html;
		exec = Executors.newFixedThreadPool(5);
		completionService = new ExecutorCompletionService<Image>(exec);
	}

	public void rende() {
		String[] infos = html.split(",");
		List<String> textInfos = new ArrayList<String>();
		List<String> imageInfos = new ArrayList<String>();

		for (String info : infos) {
			if (info.startsWith("txt.")) {
				textInfos.add(info);
			} else if (info.startsWith("img.")) {
				imageInfos.add(info);

				final Image image = new Image(info);
				completionService.submit(new Callable<Image>() {
					@Override
					public Image call() throws Exception {
						image.download();
						return image;
					}

				});
			}
		}

		rendeText(textInfos);
		rendeImage(imageInfos);

	}

	private void rendeImage(List<String> imageInfos) {
		for (String img : imageInfos) {
			try {
				System.out.println("[IMAGE]" + img + "-"
						+ completionService.take().get());

			} catch (InterruptedException ignored) {

			} catch (ExecutionException ignored) {

			}
		}
	}

	private void rendeText(List<String> textInfos) {
		for (String txt : textInfos) {
			System.out.println("[TEXT]" + txt);
		}
	}

	public void close() {
		exec.shutdown();
	}
}
