package tests.paylink.xml;

import com.jcraft.jsch.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tmp_log {

  public static void testLog() {
    JSch jsch = new JSch();
    Session session = null;
    try {
      session = jsch.getSession("semyvolos_h", "172.29.105.72", 22);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      Channel channel = session.openChannel("sftp");
      channel.connect();
      ChannelSftp sftpChannel = (ChannelSftp) channel;

      InputStream stream = sftpChannel.get("/opt/tomee/paylink/conf/web.xml");
      try {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = br.readLine()) != null) {
          System.out.println(line);
        }

      } catch (IOException io) {
        System.out.println(
            "Exception occurred during reading file from SFTP server due to " + io.getMessage());
        io.getMessage();

      } catch (Exception e) {
        System.out.println(
            "Exception occurred during reading file from SFTP server due to " + e.getMessage());
        e.getMessage();
      }

      sftpChannel.exit();
      session.disconnect();
    } catch (JSchException e) {
      e.printStackTrace();
    } catch (SftpException e) {
      e.printStackTrace();
    }
  }
}
