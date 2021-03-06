package fr.umlv.lastproject.smart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import fr.umlv.lastproject.smart.utils.SmartConstants;
import fr.umlv.lastproject.smart.utils.SmartLogger;

public class LayerItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private boolean visible;
	private Bitmap overview;

	private boolean isEditable;

	private final Logger logger = SmartLogger.getLocator().getLogger();

	/**
	 * 
	 * @param name
	 *            of the layer
	 * @param visible
	 *            true or false
	 * @param overview
	 *            of the symbologie
	 */
	public LayerItem(String name, boolean visible, Bitmap overview,
			boolean isEditable) {
		this.name = name;
		this.visible = visible;
		this.overview = overview;
		this.isEditable = isEditable;
	}

	/**
	 * 
	 * @param name
	 *            of the layer
	 * @param overview
	 *            of the symbologie
	 */
	public LayerItem(String name, Bitmap overview, boolean isEditable) {
		this(name, true, overview, isEditable);
	}

	/**
	 * 
	 * @return the name of the layer
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return visible or not
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * 
	 * @param visible
	 *            or not
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEditable() {
		return this.isEditable;
	}

	/**
	 * 
	 * @return overview of the symbo
	 */
	public Bitmap getOverview() {
		return overview;
	}

	public void setOverview(Bitmap overview) {
		this.overview = overview;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((overview == null) ? 0 : overview.hashCode());
		result = prime * result + (visible ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LayerItem other = (LayerItem) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (overview == null) {
			if (other.overview != null) {
				return false;
			}
		} else if (!overview.equals(other.overview)) {
			return false;
		}
		if (visible != other.visible) {
			return false;
		}
		return true;
	}

	private static final int QUALITY = 100;

	/**
	 * 
	 * @param out
	 *            the object to get
	 * @throws IOException
	 *             if canot read
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(name);
		out.writeBoolean(visible);
		final File appFolder = new File(SmartConstants.TMP_PATH);
		appFolder.mkdirs();

		final String fileName = SmartConstants.TMP_PATH + name + ".png";
		final FileOutputStream stream = new FileOutputStream(new File(fileName));
		try {
			overview.compress(Bitmap.CompressFormat.PNG, QUALITY, stream);
		} catch (OutOfMemoryError error) {
			logger.log(Level.SEVERE, "Bitmap compression error");
		}

		stream.flush();
		stream.close();
		out.writeObject(fileName);
		out.writeBoolean(isEditable);
		out.close();

	}

	/**
	 * 
	 * @param in
	 *            object to read
	 * @throws IOException
	 *             if object not readable
	 * @throws ClassNotFoundException
	 *             if class does not exist
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.name = (String) in.readObject();
		this.visible = in.readBoolean();

		try {
			this.overview = BitmapFactory.decodeFile((String) in.readObject());

		} catch (OutOfMemoryError error) {

			this.overview = null;
		}
		this.isEditable = in.readBoolean();
		in.close();

	}

}
