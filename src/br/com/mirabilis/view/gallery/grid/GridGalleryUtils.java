package br.com.mirabilis.view.gallery.grid;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout.LayoutParams;

/**
 * Classe com métodos usados em vários pontos da biblioteca
 */
class GridGalleryUtils {

	private static AnimationSet anim;
	private static LayoutParams itemParams;

	/**
	 * @param text
	 * @return true se o texto nao for nulo ou vazio
	 */
	static boolean hasText(String text) {

		return text != null && text.trim().length() > 0;
	}

	/**
	 * @return ScaleAnimation que produz efeito de clique
	 */
	static Animation getClickAnimation(Context ctx) {

		if (anim == null) {

			AlphaAnimation a = new AlphaAnimation(0.5f, 1);
			a.setDuration(250);
			a.setInterpolator(new DecelerateInterpolator());

			ScaleAnimation s = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			s.setDuration(300);
			s.setInterpolator(new AccelerateInterpolator());

			anim = new AnimationSet(false);
			anim.addAnimation(a);
			anim.addAnimation(s);
		}

		return anim;
	}

	/**
	 * @param width
	 * @param height
	 * @return LayoutParams com margens de 5dp
	 */
	static LayoutParams getLayoutParams(int width, int height) {

		itemParams = new LayoutParams(width - 10, height - 10);
		itemParams.setMargins(5, 5, 5, 5);

		return itemParams;
	}
}
