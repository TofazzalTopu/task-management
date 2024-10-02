package basico.task.management.service.assets;

import java.util.List;

import basico.task.management.model.primary.Assets;
import basico.task.management.model.primary.Status;

public interface AssetsExternalService  {

    List<Assets> AssetListPrecarious(Status status);

    List<Assets> assetListSquatted(Status status);
}
