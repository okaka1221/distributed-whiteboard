package client;

import org.kohsuke.args4j.Option;

public class JoinWhiteBoardArgs {
	@Option(name = "-a", required = true, usage = "Server Address")
    private String address;
	
	@Option(name = "-p", required = true, usage = "Server Port number")
    private int port;
	
	@Option(name = "-n", required = true, usage = "Username")
    private String username;
    
	public String getHost() {
    	return address;
    }
	
    public int getPort() {
    	return port;
    }
    
    public String getUsername() {
    	return username;
    }
}
