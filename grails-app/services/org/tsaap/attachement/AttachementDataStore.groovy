package org.tsaap.attachement

import org.tsaap.uploadImage.DataStoreException
import org.tsaap.uploadImage.FileDataStore

/**
 * Created by dylan on 13/05/15.
 */
/**
 * Classe représentant le FileDataStore utilisé pour stocker les fichiers dans
 * eliot-tdbase,...
 * Cette classe étend la classe FileDataStore fournie par Jackrabbit.
 * L'objectif est de fournir une  classe d'initialisation à Spring pour lever
 * une exception si le dossier devant contenir les fichiers n'est pas crée, ou
 * ne permet pas l'écriture des fichiers
 * @author franck Silvestre
 */
class AttachementDataStore extends FileDataStore {

    /**
     *  Classe initialisant le dataStore
     *  Si le dossier racine devant contenir les fichiers n'existe pas ou ne dispose
     *  pas des droits nécessaires alors la méthode lève une exception
     * @throws DataStoreException
     */
    def initFileDataStore() throws DataStoreException {
        if (getPath() == null) {
            def message = """
      attachementdatastore.path.null
      Verify configuration parameter 'tsaap.datastore.path'.
      """
            throw new DataStoreException(message);
        }
        File rootDir = new File(getPath());
        if (!rootDir.exists()) {
            def message = """
      attachementdatastore.path.doesnotexist : ${getPath()} .
      Modify configuration parameter 'tsaap.datastore.path'
      OR
      create ${getPath()} repository with writing right for the application.
      """
            throw new DataStoreException(message);
        }
        if (!rootDir.isDirectory()) {
            def message = """
      attachementdatastore.path.isnotdirectory : ${getPath()} .
      Modify configuration parameter 'tsaap.datastore.path'
      OR
      create ${getPath()} repository with writing right for the application.
      """
            throw new DataStoreException(message);
        }
        if (!rootDir.canWrite()) {
            def message = """
      attachementdatastore.path.nowriteaccess : ${getPath()} .
      Modify configuration parameter 'tsaap.datastore.path'
      OR
      give writing right for the application on ${getPath()} repository.
      """
            throw new DataStoreException(message);
        }
        super.init("");
    }
}
