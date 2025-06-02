/**
 * � Copyright IBM Corporation 2014.  
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. 
 */
 
 /**
 * This is prominent notice that the 7zip Library is used.
 * Its use is covered by the GNU LESSER GENERAL PUBLIC LICENSE.
 */
package com.urbancode.air.plugin.tool

import net.sf.sevenzipjbinding.IArchiveExtractCallback
import net.sf.sevenzipjbinding.InArchiveImpl
import net.sf.sevenzipjbinding.ISequentialOutStream
import net.sf.sevenzipjbinding.ExtractAskMode
import net.sf.sevenzipjbinding.SevenZipException
import net.sf.sevenzipjbinding.ExtractOperationResult
import net.sf.sevenzipjbinding.PropID

public class ExtractCallback implements IArchiveExtractCallback {
    def inArchive
    def targetDir
    def out
    def isFolder = false
    def folderCreated = false
    def filePath

    public ExtractCallback(InArchiveImpl inArchive, File targetDir) {
        this.inArchive = inArchive
        this.targetDir = targetDir
    }

    public ISequentialOutStream getStream(int index, ExtractAskMode extractAskMode) throws SevenZipException {
        isFolder = inArchive.getProperty(index, PropID.IS_FOLDER)
        filePath = inArchive.getProperty(index, PropID.PATH)
        if (extractAskMode == ExtractAskMode.EXTRACT && !isFolder) {
            return new ISequentialOutStream() {
                public int write(byte[] data) throws SevenZipException {
                    out.write(data)
                    return data.length
                }
            }
        }
    }

    public void prepareOperation(ExtractAskMode extractAskMode)
    throws SevenZipException {
        if (extractAskMode == ExtractAskMode.EXTRACT) {
            def file = new File(targetDir, filePath)
            file.getParentFile().mkdirs()
            if (isFolder) {
                folderCreated = file.mkdirs()
            }
            else {
                out = new BufferedOutputStream(new FileOutputStream(file))
            }
        }
    }

    public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
        if (extractOperationResult != ExtractOperationResult.OK) {
            println("Extraction error: " + extractOperationResult.toString())
        }
        else if (isFolder) {
            if (folderCreated) println("Created directory " + filePath)
        }
        else {
            println("Extracted " + filePath)
        }

        if (out) {
            out.flush()
            out.close()
        }
    }

    public void setCompleted(long completeValue) throws SevenZipException {
    }

    public void setTotal(long total) throws SevenZipException {
    }

}
