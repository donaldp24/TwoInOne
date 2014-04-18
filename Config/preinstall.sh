#!/system/bin/busybox sh

BUSYBOX="/system/bin/busybox"

if [ ! -e /data/system.notfirstrun ]; then	
  echo "do preinstall job"	
  
	/system/bin/sh /system/bin/pm preinstall /system/preinstall
	/system/bin/sh /system/bin/pm preinstall /sdcard/preinstall

	# copy android modify tool files
	mkdir /mnt/nanda
	mount -t vfat /dev/block/nanda /mnt/nanda
	$BUSYBOX cp /mnt/nanda/vendor/system/build.prop /system/
	$BUSYBOX cp /mnt/nanda/vendor/system/media/bootanimation.zip /system/media/
	$BUSYBOX cp /mnt/nanda/vendor/system/usr/keylayout/*.kl /system/usr/keylayout/
	sync
	umount /mnt/nanda
	rmdir /mnt/nanda

	$BUSYBOX touch /data/system.notfirstrun	
	
	 mkdir /databk
   mount -t ext4 /dev/block/nandi /databk	
   rm /databk/data_backup.tar
   umount /databk
   rmdir /databk
   echo "preinstall ok"

elif [ -e /system/data.need.backup ];then
   echo "data backup:tar /databk/data_backup.tar /data"
   mkdir /databk
   mount -t ext4 /dev/block/nandi /databk	
   
   rm /databk/data_backup.tar

   $BUSYBOX tar -cf /databk/data_backup.tar /data
   rm /system/data.need.backup
  
   umount /databk
   rmdir /databk

else 
   echo "do nothing"
fi



DNSMASQ_CONF="/data/dnsmasq.conf"

echo "dhcp-authoritative" > $DNSMASQ_CONF
echo "interface=wlan0" >> $DNSMASQ_CONF
echo "dhcp-range=192.168.43.10,192.168.43.250,12h" >> $DNSMASQ_CONF
echo "user=root" >> $DNSMASQ_CONF
echo "no-negcache" >> $DNSMASQ_CONF
echo "address=/#/192.168.43.1" >> $DNSMASQ_CONF

iptables -t nat -A PREROUTING -p udp --dport 53 -j REDIRECT
iptables -t nat -A PREROUTING -p tcp --dport 80 -j DNAT --to-destination 192.168.43.1:8080

chmod 644 $DNSMASQ_CONF
mkdir /var
mkdir /var/run
dnsmasq -C $DNSMASQ_CONF

#to enable wifi
svc wifi enable

#to configure wifi ip address as ...
ifconfig wlan0 192.168.43.1 up

#to start softap
hostapd -B /data/hostapd.conf