/**
 * 
 */
package com.lakj.comspace.eclipse.info.actions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.PropertyResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Version;

import com.lakj.comspace.eclipse.info.Activator;

/**
 * The utility class to find Eclipse IDE Information.
 * 
 * @author Lak J Comspace
 *
 */
public class InfoUtil {

	/**
	 * This method finds and shows the Eclipse IDE Information in a message box.
	 */
	public static String findIdeInfo() {

		StringBuilder infoStringBuilder = new StringBuilder();

		IProduct eclipseProduct = Platform.getProduct();

		// IDE name.
		String ideName = eclipseProduct.getName();
		infoStringBuilder.append("IDE Name: ").append(ideName).append("\n");

		// IDE version.
		// Here we use the way
		// org.eclipse.ui.internal.ProductProperties.getAboutText() has used to
		// find version number.

		URL location = FileLocator.find(eclipseProduct.getDefiningBundle(), new Path("$nl$/about.mappings"), null);

		if (location != null) {

			StringBuilder ideVersionBuilder = new StringBuilder();

			try (InputStream is = location.openStream()) {

				PropertyResourceBundle bundle = new PropertyResourceBundle(is);

				try {
					String versionName = bundle.getString("1");
					ideVersionBuilder.append(versionName);
				} catch (Exception e) {
					e.printStackTrace();
				}

				String versionNumber = null;

				try {
					versionNumber = bundle.getString("2");
					ideVersionBuilder.append(" (").append(versionNumber).append(")");
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Sometimes this above step doesn't capture the version number.
				// Then we try the bellow option.
				if (versionNumber == null) {
					Version version = eclipseProduct.getDefiningBundle().getVersion();
					ideVersionBuilder.append(" (").append(version.toString()).append(")");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			String ideVersion = ideVersionBuilder.toString();
			infoStringBuilder.append("IDE Version: ").append(ideVersion).append("\n");

		}

		// Plugin version.
		String pluginVersion = Platform.getBundle(Activator.PLUGIN_ID).getVersion().toString();
		infoStringBuilder.append("LakJ Plugin Version: ").append(pluginVersion).append("\n");

		// System user name.
		String username = System.getProperty("user.name");
		infoStringBuilder.append("System Username: ").append(username);

		return infoStringBuilder.toString();

	}

}
