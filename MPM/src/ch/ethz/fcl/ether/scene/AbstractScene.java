/*
Copyright (c) 2013, ETH Zurich (Stefan Mueller Arisona, Eva Friedrich)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, 
  this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.
 * Neither the name of ETH Zurich nor the names of its contributors may be 
  used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ch.ethz.fcl.ether.scene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.ethz.fcl.ether.model.IModel;
import ch.ethz.fcl.ether.scene.IView.ViewType;
import ch.ethz.fcl.ether.ui.Button;

/**
 * Abstract scene class that implements some basic common functionality. Use as
 * base for common implementations.
 * 
 * @author radar
 * 
 */
public abstract class AbstractScene implements IScene {

	private IModel model;

	private final ArrayList<IView> views = new ArrayList<IView>();

	private final NavigationTool navigationTool = new NavigationTool();
	private final NavigationGrid navigationGrid = new NavigationGrid(10, 0.1f);

	private IView currentView = null;
	private ITool currentTool = new NullTool();

	private final List<Button> buttons = new ArrayList<Button>();

	@Override
	public IModel getModel() {
		return model;
	}

	@Override
	public void setModel(IModel model) {
		this.model = model;
	}

	@Override
	public void addView(IView view) {
		views.add(view);
		if (currentView == null)
			currentView = view;
	}

	@Override
	public List<IView> getViews() {
		return views;
	}

	@Override
	public void repaintAll() {
		for (IView view : views)
			view.repaint();
	}

	@Override
	public boolean isEnabled(IView view) {
		return true;
	}

	@Override
	public IView getCurrentView() {
		return currentView;
	}

	@Override
	public ITool getCurrentTool() {
		return currentTool;
	}

	@Override
	public void setCurrentTool(ITool tool) {
		if (tool != null)
			currentTool = tool;
		else
			currentTool = new NullTool();
		repaintAll();
	}

	@Override
	public NavigationTool getNavigationTool() {
		return navigationTool;
	}

	@Override
	public NavigationGrid getNavigationGrid() {
		return navigationGrid;
	}

	@Override
	public List<Button> getButtons() {
		return buttons;
	}

	// key listener

	@Override
	public void keyPressed(KeyEvent e, IView view) {
		updateCurrentView(view);

		// buttons have precedence over tools
		for (Button button : view.getScene().getButtons()) {
			if (button.getKey() == e.getKeyCode()) {
				button.fire(view);
				view.getScene().repaintAll();
				return;
			}
		}

		// always handle ESC (if not handled by button)
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);

		// finally, pass on to tool
		currentTool.keyPressed(e, view);
	}

	@Override
	public void keyReleased(KeyEvent e, IView view) {
	}

	@Override
	public void keyTyped(KeyEvent e, IView view) {
	}

	// mouse listener

	@Override
	public void mouseEntered(MouseEvent e, IView view) {
	}

	@Override
	public void mouseExited(MouseEvent e, IView view) {
	}

	@Override
	public void mousePressed(MouseEvent e, IView view) {
		updateCurrentView(view);

		// buttons have precedence over tools
		if (view.getViewType() == ViewType.INTERACTIVE_VIEW) {
			for (Button button : view.getScene().getButtons()) {
				if (button.hit(e.getPoint().x, e.getPoint().y, view)) {
					button.fire(view);
					view.getScene().repaintAll();
					return;
				}
			}
		}

		// handle tools (with active navigation when modifier is pressed)
		if (!isModifierDown(e))
			currentTool.mousePressed(e, view);
		else
			navigationTool.mousePressed(e, view);
	}

	@Override
	public void mouseReleased(MouseEvent e, IView view) {
		if (!isModifierDown(e))
			currentTool.mouseReleased(e, view);
		else
			navigationTool.mouseReleased(e, view);
	}

	@Override
	public void mouseClicked(MouseEvent e, IView view) {
	}

	// mouse motion listener

	@Override
	public void mouseMoved(MouseEvent e, IView view) {
		if (view.getViewType() == ViewType.INTERACTIVE_VIEW) {
			Button button = null;
			for (Button b : view.getScene().getButtons()) {
				if (b.hit(e.getPoint().x, e.getPoint().y, view)) {
					button = b;
					break;
				}
			}
			String newMessage = button != null ? button.getHelp() : null;
			if ((newMessage == null && Button.getMessage() != null) || (newMessage != null && !newMessage.equals(Button.getMessage()))) {
				Button.setMessage(newMessage);
				repaintAll();
			}
		}
		currentTool.mouseMoved(e, view);
		navigationTool.mouseMoved(e, view);
	}

	@Override
	public void mouseDragged(MouseEvent e, IView view) {
		if (!isModifierDown(e))
			currentTool.mouseDragged(e, view);
		else
			navigationTool.mouseDragged(e, view);
	}

	// mouse wheel listener

	@Override
	public void mouseWheelMoved(MouseWheelEvent e, IView view) {
		// TODO: update current view here?
		navigationTool.mouseWheelMoved(e, view);
	}

	// protected stuff

	protected final void addButton(Button button) {
		buttons.add(button);
	}

	protected final void addButtons(Collection<? extends Button> buttons) {
		this.buttons.addAll(buttons);
	}

	// private stuff

	private boolean isModifierDown(MouseEvent e) {
		return e.isShiftDown() || e.isControlDown() || e.isAltDown() || e.isMetaDown();
	}

	private void updateCurrentView(IView view) {
		if (currentView != view) {
			currentView = view;
			repaintAll();
		}
	}
}
