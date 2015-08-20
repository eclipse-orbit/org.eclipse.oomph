/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.recorder;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class RecorderPoliciesComposite extends Composite implements ISelectionProvider
{
  private final RecorderTransaction transaction;

  private final boolean clean;

  private final CheckboxTableViewer viewer;

  private ViewerFilter filter;

  private String[] sortedKeys;

  public RecorderPoliciesComposite(Composite parent, int style, RecorderTransaction transaction, boolean clean)
  {
    super(parent, SWT.NONE);
    this.transaction = transaction;
    this.clean = clean;

    setLayout(new FillLayout(SWT.HORIZONTAL));
    TableColumnLayout tableLayout = new TableColumnLayout();

    Composite composite = new Composite(this, SWT.NONE);
    composite.setLayout(tableLayout);

    viewer = CheckboxTableViewer.newCheckList(composite, style);
    viewer.setContentProvider(new ArrayContentProvider());
    viewer.setLabelProvider(new LabelProvider());
    viewer.addCheckStateListener(new ICheckStateListener()
    {
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        String path = (String)event.getElement();
        boolean policy = event.getChecked();
        RecorderPoliciesComposite.this.transaction.setPolicy(path, policy);
      }
    });

    viewer.setCheckStateProvider(new ICheckStateProvider()
    {
      public boolean isGrayed(Object element)
      {
        return false;
      }

      public boolean isChecked(Object element)
      {
        return Boolean.TRUE.equals(RecorderPoliciesComposite.this.transaction.getPolicy(element.toString()));
      }
    });

    TableColumn column = new TableColumn(viewer.getTable(), SWT.LEFT);
    tableLayout.setColumnData(column, new ColumnWeightData(100));

    refresh();

    if (sortedKeys.length != 0)
    {
      viewer.setSelection(new StructuredSelection(sortedKeys[0]));
    }
  }

  public final void refresh()
  {
    Map<String, Boolean> policies = transaction.getPolicies(clean);

    sortedKeys = policies.keySet().toArray(new String[policies.size()]);
    Arrays.sort(sortedKeys);

    viewer.setInput(sortedKeys);
  }

  public final void setFilter(ViewerFilter filter)
  {
    this.filter = filter;
    viewer.setFilters(filter == null ? new ViewerFilter[0] : new ViewerFilter[] { filter });
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    viewer.getTable().setEnabled(enabled);
  }

  public CheckboxTableViewer getViewer()
  {
    return viewer;
  }

  public String getFirstVisiblePolicy()
  {
    if (sortedKeys != null && sortedKeys.length != 0)
    {
      if (filter == null)
      {
        return sortedKeys[0];
      }

      for (String key : sortedKeys)
      {
        if (filter.select(viewer, sortedKeys, key))
        {
          return key;
        }
      }
    }

    return null;
  }

  public void selectFirstPolicy()
  {
    viewer.getTable().setSelection(0);
  }

  public void addSelectionChangedListener(ISelectionChangedListener listener)
  {
    viewer.addSelectionChangedListener(listener);
  }

  public void removeSelectionChangedListener(ISelectionChangedListener listener)
  {
    viewer.removeSelectionChangedListener(listener);
  }

  public IStructuredSelection getSelection()
  {
    return (IStructuredSelection)viewer.getSelection();
  }

  public void setSelection(ISelection selection)
  {
    viewer.setSelection(selection);
  }

  public void setSelection(ISelection selection, boolean reveal)
  {
    viewer.setSelection(selection, reveal);
  }

  public void addCheckStateListener(ICheckStateListener listener)
  {
    viewer.addCheckStateListener(listener);
  }

  public void removeCheckStateListener(ICheckStateListener listener)
  {
    viewer.removeCheckStateListener(listener);
  }

  public boolean getChecked(Object element)
  {
    return viewer.getChecked(element);
  }

  public Object[] getCheckedElements()
  {
    return viewer.getCheckedElements();
  }

  public void setAllChecked(boolean state)
  {
    viewer.setAllChecked(state);
  }

  public boolean setChecked(Object element, boolean state)
  {
    return viewer.setChecked(element, state);
  }

  public void setCheckedElements(Object[] elements)
  {
    viewer.setCheckedElements(elements);
  }

  @Override
  public boolean setFocus()
  {
    return viewer.getTable().setFocus();
  }

  @Override
  protected void checkSubclass()
  {
    // Disable the check that prevents subclassing of SWT components
  }
}
