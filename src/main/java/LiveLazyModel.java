import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "LiveLazyModel")
@ViewScoped
public class LiveLazyModel extends LazyDataModel {

    private Live selectedRow;

    @Override
    public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
        List list = addToList(first, pageSize);
        this.setRowCount(100);
        this.setPageSize(pageSize);
        return list;
    }


    private List addToList(int first, int pageSize) {
        List list = new ArrayList();
        for (Integer i = first; i < first + pageSize; i++) {
            list.add(new Live(i));
        }
        return list;
    }

    public StreamedContent getliveStreamedContent()  throws IOException {
        String idFrom = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("liveId");
        if(selectedRow != null){
            InputStream targetStream = IOUtils.toInputStream(selectedRow.id + (Double.toString(Math.random()).substring(1)));
            return new DefaultStreamedContent(targetStream, "txt", "idLiveFromSelectedRow" + selectedRow.id + ".txt");
        }
        if (idFrom != null) {
            InputStream targetStream = IOUtils.toInputStream(idFrom + (Double.toString(Math.random()).substring(1)));
            return new DefaultStreamedContent(targetStream, "txt", "idLiveFromFacesContext" + idFrom + ".txt");
        }
        throw new RuntimeException("liveId is null & selectedRow is null  ");
    }

    public void setSelectedRow(Live selectedRow) {
        this.selectedRow = selectedRow;
    }
}
