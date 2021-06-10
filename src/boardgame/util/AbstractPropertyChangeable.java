package boardgame.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractPropertyChangeable {

	protected PropertyChangeSupport propertyChangeSupport;
	
	public AbstractPropertyChangeable() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public PropertyChangeSupport getChangeSupport() {
		return propertyChangeSupport;
	}
}
