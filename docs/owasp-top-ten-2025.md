# OWASP Top Ten 2025

The **OWASP Top Ten** is a standard awareness document for developers and web application security professionals. It represents a broad consensus on the most critical security risks to web applications.  

The list is updated approximately every five years, reflecting new vulnerabilities, shifts in prevalence, and changes in industry priorities. Rankings are based on contributed data, security surveys, and community input.


## Top Ten for 2025

**A01:2025 – Broken Access Control**  
Remains at #1 as the most serious risk. Data shows that 3.73% of applications tested had one or more of the 40 CWEs in this category. Notably, **Server-Side Request Forgery (SSRF)** has now been rolled into this category.

**A02:2025 – Security Misconfiguration**  
Moved up from #5 in 2021 to #2 in 2025. Misconfigurations are increasingly common, with 3.00% of applications tested showing issues across 16 CWEs. This reflects the growing reliance on configuration-driven application behavior.

**A03:2025 – Software Supply Chain Failures**  
An expansion of A06:2021 (Vulnerable and Outdated Components). It now covers compromises across dependencies, build systems, and distribution infrastructure. Although data shows fewer occurrences, CVEs in this category have the highest average exploitability and impact scores.

**A04:2025 – Cryptographic Failures**  
Drops from #2 to #4. About 3.80% of applications tested had one or more of the 32 CWEs here. These failures often lead to sensitive data exposure or system compromise.

**A05:2025 – Injection**  
Falls from #3 to #5. Still one of the most tested categories, with 38 CWEs and numerous CVEs. It includes issues ranging from **Cross-Site Scripting (XSS)** (high frequency, lower impact) to **SQL Injection** (lower frequency, higher impact).

**A06:2025 – Insecure Design**  
Slides from #4 to #6. Introduced in 2021, this category highlights flaws in system design. Improvements in threat modeling and secure design practices have reduced its prevalence.

**A07:2025 – Authentication Failures**  
Maintains its position at #7, with a slight name change (previously “Identification and Authentication Failures”). Standardized authentication frameworks are helping reduce occurrences, but 36 CWEs remain relevant.

**A08:2025 – Software or Data Integrity Failures**  
Continues at #8. Focuses on failures to maintain trust boundaries and verify integrity of software, code, and data artifacts, distinct from broader supply chain issues.

**A09:2025 – Security Logging & Alerting Failures**  
Retains #9, with a name change (previously “Security Logging and Monitoring Failures”). Emphasizes the importance of **alerting** in addition to logging. Strong logs without actionable alerts provide limited value.

**A10:2025 – Mishandling of Exceptional Conditions**  
New for 2025. Contains 24 CWEs related to improper error handling, logical errors, failing open, and other abnormal condition scenarios.

---

## Comparison with 2021

- **Broken Access Control** remains #1.  
- **Security Misconfiguration** rises sharply from #5 to #2.  
- **Software Supply Chain Failures** introduced as a broader category, reflecting industry concern.  
- **Cryptographic Failures** and **Injection** both drop in rank.  
- **Insecure Design** slides down as practices improve.  
- **Authentication Failures** and **Software/Data Integrity Failures** remain stable.  
- **Logging & Alerting Failures** retains its position.  
- **Mishandling of Exceptional Conditions** debuts at #10.  

---
For more details, please refer to the oficial OWASP Top ten - https://owasp.org/Top10/2025/
