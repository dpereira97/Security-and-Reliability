sudo /sbin/iptables -A OUTPUT -j ACCEPT -d 10.121.52.14,10.121.52.15,10.101.52.16,10.121.72.23,10.101.85.6,10.101.85.138,10.101.85.18,10.101.148.1,10.101.85.137
sudo /sbin/iptables -A INPUT -j ACCEPT -s 10.121.52.14,10.121.52.15,10.101.52.16,10.121.72.23,10.101.85.6,10.101.85.138,10.101.85.18,10.101.148.1,10.101.85.137

sudo /sbin/iptables -A INPUT -i lo -j ACCEPT
sudo /sbin/iptables -A OUTPUT -o lo -j ACCEPT

sudo /sbin/iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT
sudo /sbin/iptables -A OUTPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

sudo /sbin/iptables -A INPUT -s 10.101.151.5 -p icmp --icmp-type 8 -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.151.5 -p icmp --icmp-type 0 -j ACCEPT

sudo /sbin/iptables -A INPUT -p tcp --dport 23232 -j ACCEPT

sudo /sbin/iptables -A INPUT -p tcp -s 10.101.149.52/255.255.254.0 --dport 22 -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT
sudo /sbin/iptables -A OUTPUT -p tcp --sport 22 -m conntrack --ctstate ESTABLISHED -j ACCEPT

sudo /sbin/iptables -A INPUT -s 10.101.149.52/255.255.254.0 -p icmp --icmp-type 0 -j ACCEPT
sudo /sbin/iptables -A OUTPUT -d 10.101.149.52/255.255.254.0 -p icmp --icmp-type 8 -j ACCEPT

sudo /sbin/iptables -P INPUT DROP
sudo /sbin/iptables -P OUTPUT DROP

sudo /sbin/iptables -L
