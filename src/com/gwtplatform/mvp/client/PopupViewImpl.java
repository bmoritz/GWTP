package com.gwtplatform.mvp.client;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;

/**
 * A simple implementation of {@link PopupView} that can be used when the
 * widget returned by {@link #asWidget()} inherits from {@link PopupPanel}.  
 * 
 * Also, this implementation simply disregards every call to
 * {@link #setContent(Object, Widget)}, {@link #addContent(Object, Widget)},
 * and {@link #clearContent(Object)}.
 * 
 * @author Philippe Beaudoin
 */
public abstract class PopupViewImpl extends ViewImpl implements PopupView {  

  private final EventBus eventBus;
  
  private HandlerRegistration closeHandlerRegistration = null;
  private HandlerRegistration autoHideHandler = null;

  /**
   * The {@link PopupViewImpl} class uses the {@link EventBus} to listen to
   * {@link NavigationEvent} in order to automatically close when this event
   * is fired, if desired. See {@link #setAutoHideOnNavigationEventEnabled(boolean)}
   * for details.
   * 
   * @param eventBus The {@link EventBus}.
   */
  protected PopupViewImpl( EventBus eventBus ) {
      this.eventBus = eventBus;
  }
  
  @Override
  public void show() {
    asPopupPanel().show();
  }

  @Override
  public void hide() {
    asPopupPanel().hide();    
  }

  @Override
  public void center() {
      doCenter();
      // We center again in a deferred command to solve a bug in IE where newly
      // created window are sometimes not centered.
      DeferredCommand.addCommand( new Command() {
        @Override
        public void execute( ) {
            doCenter();
        }
      } );
  }
  
  /**
   * This method centers the popup panel, temporarily making it visible if needed.
   */
  private void doCenter() {
    boolean wasVisible = asPopupPanel().isVisible();
    asPopupPanel().center();
    if( !wasVisible )
      asPopupPanel().hide();      
  }

  @Override
  public void setPosition( int left, int top ) {
    asPopupPanel().setPopupPosition(left, top);
  }

  @Override
  public void setCloseHandler(
      final PopupViewCloseHandler popupViewCloseHandler) {
    if( closeHandlerRegistration != null )
      closeHandlerRegistration.removeHandler();
    if( popupViewCloseHandler == null )
      closeHandlerRegistration = null;
    else {
      closeHandlerRegistration = 
        asPopupPanel().addCloseHandler( new CloseHandler<PopupPanel>() {      
          @Override
          public void onClose(CloseEvent<PopupPanel> event) {
            popupViewCloseHandler.onClose();
          }
        } );
    }
  }

  @Override
  public void setAutoHideOnNavigationEventEnabled( boolean autoHide ) {
      if( autoHide ) {      
          if( autoHideHandler != null ) 
              return;
          autoHideHandler = eventBus.addHandler( NavigationEvent.getType(), new NavigationHandler(){
            @Override
            public void onNavigation( NavigationEvent navigationEvent ) {
                hide();
            }
          } );
      }
      else {
          if( autoHideHandler != null )
              autoHideHandler.removeHandler( );
      }
  }

  /**
   * Retrieves this view as a {@link PopupPanel}. See {@link #asWidget()}.
   * 
   * @return This view as a {@link PopupPanel} object.
   */
  protected PopupPanel asPopupPanel() {
    return (PopupPanel)asWidget();
  }
}