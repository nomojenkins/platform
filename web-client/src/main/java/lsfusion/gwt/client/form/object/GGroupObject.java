package lsfusion.gwt.client.form.object;

import lsfusion.gwt.client.base.Pair;
import lsfusion.gwt.client.base.jsni.HasNativeSID;
import lsfusion.gwt.client.base.size.GSize;
import lsfusion.gwt.client.form.design.GContainer;
import lsfusion.gwt.client.form.design.GFontMetrics;
import lsfusion.gwt.client.form.filter.user.GFilter;
import lsfusion.gwt.client.form.filter.user.GFilterControls;
import lsfusion.gwt.client.form.object.table.GToolbar;
import lsfusion.gwt.client.form.object.table.grid.GGrid;
import lsfusion.gwt.client.form.object.table.grid.view.GListViewType;
import lsfusion.gwt.client.form.object.table.tree.GTreeGroup;
import lsfusion.gwt.client.form.property.*;

import java.io.Serializable;
import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class GGroupObject implements Serializable, HasNativeSID {
    public List<GObject> objects = new ArrayList<>();

    public GContainer filtersContainer;
    public GFilterControls filtersControls;
    public List<GFilter> filters = new ArrayList<>();
    
    public GGrid grid;
    public GToolbar toolbar;

    public int ID;
    public String nativeSID;
    public String sID;

    public GClassViewType viewType;
    public GListViewType listViewType;
    public GPivotOptions pivotOptions;
    public String customRenderFunction;
    public String mapTileProvider;

    public boolean asyncInit;

    public boolean isRecursive;
    public GTreeGroup parent;
    public List<GGroupObject> upTreeGroups = new ArrayList<>();

    public boolean isMap;
    public boolean isCalendarDate;
    public boolean isCalendarDateTime;
    public boolean isCalendarPeriod;

    public boolean hasHeaders;
    public boolean hasFooters;

    public boolean enableManualUpdate;

    public GRowBackgroundReader rowBackgroundReader;
    public GRowForegroundReader rowForegroundReader;
    public GCustomOptionsReader customOptionsReader;

    // transient
    private transient GSize columnSumWidth;
    public GSize getColumnSumWidth() {
        if(columnSumWidth == null) {
            columnSumWidth = GSize.ZERO;
        }
        return columnSumWidth;
    }
    public void setColumnSumWidth(GSize columnSumWidth) {
        this.columnSumWidth = columnSumWidth;
    }

    public transient int columnCount;

    public transient boolean highlightDuplicateValue;

    private transient GSize rowMaxHeight;
    public GSize getRowMaxHeight() {
        if(rowMaxHeight == null) {
            rowMaxHeight = GSize.ZERO;
        }
        return rowMaxHeight;
    }
    public void setRowMaxHeight(GSize rowMaxHeight) {
        this.rowMaxHeight = rowMaxHeight;
    }

    public Pair<GSize, GSize> getSize(int lines, int columns, GSize extraWidth, GSize headerHeight) {
        if(lines == -1)
            lines = 5;

        int columnCount = this.columnCount;
        if(columns == -1)
            columns = Math.min(columnCount <= 3 ? columnCount : (int) round(3 + pow(columnCount - 6, 0.7)), 6);

        Pair<GSize, GSize> gridPaddings = GFontMetrics.getGridPaddings(lines, columns, hasHeaders, hasFooters);
        return new Pair<>(
                (columnCount > 0 ? columnSumWidth.scale(columns).div(columnCount).add(gridPaddings.first) : GSize.ZERO).add(extraWidth),
                getRowMaxHeight().scale(lines).add(gridPaddings.second).add(headerHeight)); // actually it is the max header height, so it's not that accurate, however for now it doesn't matter that much
    }

    public String getCaption() {
        if (objects.isEmpty()) {
            //todo: локализовать попозже через GWT-шный Messages interface
            return "Empty group";
        }

        String result = "";
        for (GObject object : objects) {
            if (!result.isEmpty()) {
                result += ", ";
            }
            result += object.getCaption();
        }
        return result;
    }

    public String getSID() {
        return sID;
    }

    @Override
    public String getNativeSID() {
        return nativeSID;
    }

    public boolean mayHaveChildren() {
        return isRecursive || (parent != null && parent.groups.indexOf(this) != parent.groups.size() - 1);
    }

    public GGroupObject getUpTreeGroup() {
        if (upTreeGroups.size() > 0)
            return upTreeGroups.get(upTreeGroups.size() - 1);
        else
            return null;
    }

    public List<GGroupObject> getUpTreeGroups() {
        ArrayList<GGroupObject> result = new ArrayList<>(upTreeGroups);
        result.add(this);
        return result;
    }

    public boolean isLastGroupInTree() {
        boolean last = false;
        if (parent.groups.size() > 0)
            last = parent.groups.get(parent.groups.size() - 1) == this;
        return parent != null && last;
    }

    public static ArrayList<GGroupObjectValue> mergeGroupValues(LinkedHashMap<GGroupObject, ArrayList<GGroupObjectValue>> groupObjectColumnKeys) {
        if (groupObjectColumnKeys.isEmpty()) {
            return GGroupObjectValue.SINGLE_EMPTY_KEY_LIST;
        } else if (groupObjectColumnKeys.size() == 1) {
            return groupObjectColumnKeys.values().iterator().next();
        }

        //находим декартово произведение ключей колонок
        ArrayList<GGroupObjectValue> propColumnKeys = GGroupObjectValue.SINGLE_EMPTY_KEY_LIST;
        for (Map.Entry<GGroupObject, ArrayList<GGroupObjectValue>> entry : groupObjectColumnKeys.entrySet()) {
            ArrayList<GGroupObjectValue> groupColumnKeys = entry.getValue(); // mutable

            if(propColumnKeys.isEmpty()) {
                propColumnKeys = new ArrayList<>(groupColumnKeys);
            } else {
                ArrayList<GGroupObjectValue> newPropColumnKeys = new ArrayList<>();
                for (GGroupObjectValue propColumnKey : propColumnKeys) {
                    for (GGroupObjectValue groupObjectKey : groupColumnKeys) {
                        newPropColumnKeys.add(GGroupObjectValue.getFullKey(propColumnKey, groupObjectKey));
                    }
                }
                propColumnKeys = newPropColumnKeys;
            }
        }

        return propColumnKeys;
    }

    public GGroupObjectValue filterRowKeys(GGroupObjectValue fullCurrentKey) {
        return fullCurrentKey.filter(Collections.singletonList(this));
    }
}
