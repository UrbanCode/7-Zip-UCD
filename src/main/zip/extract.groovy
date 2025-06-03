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

import com.urbancode.air.plugin.utils.AirPluginTool
import com.urbancode.air.plugin.utils.ExtractCallback
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream
import net.sf.sevenzipjbinding.SevenZipException
import net.sf.sevenzipjbinding.SevenZip

def workDir = new File('.').canonicalFile
final def apTool = new AirPluginTool(this.args[0], this.args[1])
final def props = apTool.getStepProperties()
def dirOffset = props['dirOffset']
def include = props['include']
def exclude = props['exclude']
def targetDir = props['targetDir']

if (dirOffset) {
    workDir = new File(workDir, dirOffset)
}

println "Directory Offset: " + dirOffset
println "Include files: " + include
println "Exclude files: " + exclude
println "Extract location: " + targetDir

// search for files to process
final def ant = new AntBuilder()
def scanner = ant.fileScanner {
    if (exclude && exclude.trim().length() > 0) {
        fileset(dir: "${workDir.canonicalPath}", includes: "${include.split('\n').join(' ')}", excludes: "${exclude?.split('\n')?.join(' ')}")
    }
    else {
        fileset(dir: "${workDir.canonicalPath}", includes: "${include.split('\n').join(' ')}")
    }
}

if (!scanner.hasFiles()) {
    println "Did not find any files matching \"${include.split('\n').join(' ')}\" except \"${exclude?.split('\n')?.join(' ')}\" in ${workDir.canonicalPath}!"
}
else {
    def outFile = new File(targetDir)
    if (outFile.exists() && !outFile.isDirectory()) throw new Exception("Target directory $targetDir is a file!")
    outFile.mkdirs()
    for (File file in scanner) {
        println "--------------------------------------------------------"
        println "Processing archive $file"
        def randomAccessFile
        def inArchive
        try {
            randomAccessFile = new RandomAccessFile(file, 'r')
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))

            def itemsToExtract = [] as List

            for (i in 0..inArchive.numberOfItems - 1) {
                itemsToExtract << i
            }
            inArchive.extract(itemsToExtract as int[], false, new ExtractCallback(inArchive, outFile))

        }
        catch (Exception e) {
            println("Error extracting archive: " + e)
            System.exit(1)
        }
        finally {
            if (inArchive) {
                try {
                    inArchive.close()
                }
                catch (SevenZipException e) {
                }
            }
            if (randomAccessFile) {
                try {
                    randomAccessFile.close()
                }
                catch (IOException e) {
                }
            }
        }
    }
}

System.exit 0
     
