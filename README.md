# XenMC
Minecraft XenForo IP-based Registration

### Installation
Installation Steps:

* Download and unzip XenMC.zip
* Upload XenMC.jar to your server
* Restart your server, and edit XenMC/config.yml to match your XenForo database settings (Make sure your server has access to the database!)
* Upload the /XenForo/library folder to your root XenForo directory
* Navigate to XFCP -> Addons -> Install Addon
* Choose "Install from uploaded file", use the file XenForo/addon-XenMC.xml
* Install the Addon

Verify the addon is working by logging on to the server, then registering for an account.

### Notes
* Currently, the addon will not process name changes, so those must be done manually
* You MUST have mod_cloudflare installed if you are using Cloudflare! Otherwise, no one will be able to register!
