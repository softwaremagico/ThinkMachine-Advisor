# By default, the flags in this file are appended to flags specified
# in /usr/share/android-studio/data/sdk/tools/proguard/proguard-android.txt

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

##---------------Begin: proguard configuration common for all Android apps ----------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-dontnote com.android.vending.licensing.ILicensingService

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
##---------------End: proguard configuration common for all Android apps ----------

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

##---------------End: proguard configuration for Gson  ----------

# Software Magico
-keep class com.softwaremagico.*.** { *; }
-keep class com.itextpdf.*.** { *; }


## ---- Missing classes in v 8.0

-dontwarn com.sun.jdi.Bootstrap
-dontwarn com.sun.jdi.ReferenceType
-dontwarn com.sun.jdi.VirtualMachine
-dontwarn com.sun.jdi.VirtualMachineManager
-dontwarn com.sun.jdi.connect.AttachingConnector
-dontwarn com.sun.jdi.connect.Connector$Argument
-dontwarn com.sun.jdi.connect.Connector
-dontwarn com.sun.jdi.event.Event
-dontwarn com.sun.jdi.event.EventIterator
-dontwarn com.sun.jdi.event.EventQueue
-dontwarn com.sun.jdi.event.EventSet
-dontwarn com.sun.jdi.event.MethodEntryEvent
-dontwarn com.sun.jdi.request.EventRequestManager
-dontwarn com.sun.jdi.request.MethodEntryRequest
-dontwarn com.sun.tools.attach.VirtualMachine
-dontwarn java.applet.Applet
-dontwarn java.lang.Module
-dontwarn java.lang.instrument.ClassDefinition
-dontwarn java.lang.instrument.UnmodifiableClassException
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn javax.lang.model.element.Modifier
-dontwarn javax.mail.Address
-dontwarn javax.mail.Authenticator
-dontwarn javax.mail.BodyPart
-dontwarn javax.mail.Message$RecipientType
-dontwarn javax.mail.Message
-dontwarn javax.mail.Multipart
-dontwarn javax.mail.PasswordAuthentication
-dontwarn javax.mail.Session
-dontwarn javax.mail.Transport
-dontwarn javax.mail.internet.AddressException
-dontwarn javax.mail.internet.InternetAddress
-dontwarn javax.mail.internet.MimeBodyPart
-dontwarn javax.mail.internet.MimeMessage
-dontwarn javax.mail.internet.MimeMultipart
-dontwarn javax.management.InstanceNotFoundException
-dontwarn javax.management.MBeanRegistrationException
-dontwarn javax.management.MBeanServer
-dontwarn javax.management.MalformedObjectNameException
-dontwarn javax.management.ObjectInstance
-dontwarn javax.management.ObjectName
-dontwarn javax.naming.Context
-dontwarn javax.naming.InitialContext
-dontwarn javax.naming.NamingException
-dontwarn javax.servlet.Filter
-dontwarn javax.servlet.FilterChain
-dontwarn javax.servlet.FilterConfig
-dontwarn javax.servlet.ServletContainerInitializer
-dontwarn javax.servlet.ServletContext
-dontwarn javax.servlet.ServletContextEvent
-dontwarn javax.servlet.ServletContextListener
-dontwarn javax.servlet.ServletRequest
-dontwarn javax.servlet.ServletResponse
-dontwarn javax.servlet.http.HttpServlet
-dontwarn javax.servlet.http.HttpServletRequest
-dontwarn javax.servlet.http.HttpServletResponse
-dontwarn javax.xml.crypto.XMLCryptoContext
-dontwarn javax.xml.crypto.dom.DOMCryptoContext
-dontwarn javax.xml.crypto.dom.DOMStructure
-dontwarn javax.xml.crypto.dsig.CanonicalizationMethod
-dontwarn javax.xml.crypto.dsig.DigestMethod
-dontwarn javax.xml.crypto.dsig.Reference
-dontwarn javax.xml.crypto.dsig.SignatureMethod
-dontwarn javax.xml.crypto.dsig.SignedInfo
-dontwarn javax.xml.crypto.dsig.Transform
-dontwarn javax.xml.crypto.dsig.XMLObject
-dontwarn javax.xml.crypto.dsig.XMLSignContext
-dontwarn javax.xml.crypto.dsig.XMLSignature
-dontwarn javax.xml.crypto.dsig.XMLSignatureFactory
-dontwarn javax.xml.crypto.dsig.dom.DOMSignContext
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyInfo
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyValue
-dontwarn javax.xml.crypto.dsig.keyinfo.X509Data
-dontwarn javax.xml.crypto.dsig.spec.C14NMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.DigestMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.TransformParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.XPathType$Filter
-dontwarn javax.xml.crypto.dsig.spec.XPathType
-dontwarn javax.xml.stream.Location
-dontwarn javax.xml.stream.XMLEventReader
-dontwarn javax.xml.stream.XMLInputFactory
-dontwarn javax.xml.stream.XMLStreamException
-dontwarn javax.xml.stream.events.Characters
-dontwarn javax.xml.stream.events.EndElement
-dontwarn javax.xml.stream.events.StartElement
-dontwarn javax.xml.stream.events.XMLEvent
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMKeyInfoFactory
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMReference
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMSignedInfo
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMUtils
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMXMLSignature
-dontwarn org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI
-dontwarn org.apache.xml.security.utils.Base64
-dontwarn org.codehaus.janino.ClassBodyEvaluator
-dontwarn org.codehaus.janino.ScriptEvaluator
-dontwarn org.dom4j.Document
-dontwarn org.dom4j.DocumentFactory
-dontwarn org.dom4j.Element
-dontwarn org.dom4j.Node
-dontwarn org.dom4j.io.OutputFormat
-dontwarn org.dom4j.io.SAXReader
-dontwarn org.dom4j.io.XMLWriter
-dontwarn org.jboss.vfs.VirtualFile
-dontwarn org.spongycastle.asn1.ASN1Encodable
-dontwarn org.spongycastle.asn1.ASN1EncodableVector
-dontwarn org.spongycastle.asn1.ASN1Enumerated
-dontwarn org.spongycastle.asn1.ASN1InputStream
-dontwarn org.spongycastle.asn1.ASN1Integer
-dontwarn org.spongycastle.asn1.ASN1ObjectIdentifier
-dontwarn org.spongycastle.asn1.ASN1OctetString
-dontwarn org.spongycastle.asn1.ASN1OutputStream
-dontwarn org.spongycastle.asn1.ASN1Primitive
-dontwarn org.spongycastle.asn1.ASN1Sequence
-dontwarn org.spongycastle.asn1.ASN1Set
-dontwarn org.spongycastle.asn1.ASN1String
-dontwarn org.spongycastle.asn1.ASN1TaggedObject
-dontwarn org.spongycastle.asn1.DERIA5String
-dontwarn org.spongycastle.asn1.DERNull
-dontwarn org.spongycastle.asn1.DERObjectIdentifier
-dontwarn org.spongycastle.asn1.DEROctetString
-dontwarn org.spongycastle.asn1.DEROutputStream
-dontwarn org.spongycastle.asn1.DERSequence
-dontwarn org.spongycastle.asn1.DERSet
-dontwarn org.spongycastle.asn1.DERTaggedObject
-dontwarn org.spongycastle.asn1.cmp.PKIFailureInfo
-dontwarn org.spongycastle.asn1.cms.Attribute
-dontwarn org.spongycastle.asn1.cms.AttributeTable
-dontwarn org.spongycastle.asn1.cms.ContentInfo
-dontwarn org.spongycastle.asn1.cms.EncryptedContentInfo
-dontwarn org.spongycastle.asn1.cms.EnvelopedData
-dontwarn org.spongycastle.asn1.cms.IssuerAndSerialNumber
-dontwarn org.spongycastle.asn1.cms.KeyTransRecipientInfo
-dontwarn org.spongycastle.asn1.cms.OriginatorInfo
-dontwarn org.spongycastle.asn1.cms.RecipientIdentifier
-dontwarn org.spongycastle.asn1.cms.RecipientInfo
-dontwarn org.spongycastle.asn1.esf.OtherHashAlgAndValue
-dontwarn org.spongycastle.asn1.esf.SigPolicyQualifierInfo
-dontwarn org.spongycastle.asn1.esf.SigPolicyQualifiers
-dontwarn org.spongycastle.asn1.esf.SignaturePolicyId
-dontwarn org.spongycastle.asn1.esf.SignaturePolicyIdentifier
-dontwarn org.spongycastle.asn1.ess.ESSCertID
-dontwarn org.spongycastle.asn1.ess.ESSCertIDv2
-dontwarn org.spongycastle.asn1.ess.SigningCertificate
-dontwarn org.spongycastle.asn1.ess.SigningCertificateV2
-dontwarn org.spongycastle.asn1.ocsp.BasicOCSPResponse
-dontwarn org.spongycastle.asn1.ocsp.OCSPObjectIdentifiers
-dontwarn org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers
-dontwarn org.spongycastle.asn1.tsp.MessageImprint
-dontwarn org.spongycastle.asn1.tsp.TSTInfo
-dontwarn org.spongycastle.asn1.x500.X500Name
-dontwarn org.spongycastle.asn1.x509.AlgorithmIdentifier
-dontwarn org.spongycastle.asn1.x509.CRLDistPoint
-dontwarn org.spongycastle.asn1.x509.DistributionPoint
-dontwarn org.spongycastle.asn1.x509.DistributionPointName
-dontwarn org.spongycastle.asn1.x509.Extension
-dontwarn org.spongycastle.asn1.x509.Extensions
-dontwarn org.spongycastle.asn1.x509.GeneralName
-dontwarn org.spongycastle.asn1.x509.GeneralNames
-dontwarn org.spongycastle.asn1.x509.SubjectPublicKeyInfo
-dontwarn org.spongycastle.asn1.x509.TBSCertificateStructure
-dontwarn org.spongycastle.cert.X509CertificateHolder
-dontwarn org.spongycastle.cert.jcajce.JcaX509CertificateConverter
-dontwarn org.spongycastle.cert.jcajce.JcaX509CertificateHolder
-dontwarn org.spongycastle.cert.ocsp.BasicOCSPResp
-dontwarn org.spongycastle.cert.ocsp.CertificateID
-dontwarn org.spongycastle.cert.ocsp.CertificateStatus
-dontwarn org.spongycastle.cert.ocsp.OCSPException
-dontwarn org.spongycastle.cert.ocsp.OCSPReq
-dontwarn org.spongycastle.cert.ocsp.OCSPReqBuilder
-dontwarn org.spongycastle.cert.ocsp.OCSPResp
-dontwarn org.spongycastle.cert.ocsp.SingleResp
-dontwarn org.spongycastle.cms.CMSEnvelopedData
-dontwarn org.spongycastle.cms.Recipient
-dontwarn org.spongycastle.cms.RecipientId
-dontwarn org.spongycastle.cms.RecipientInformation
-dontwarn org.spongycastle.cms.RecipientInformationStore
-dontwarn org.spongycastle.cms.SignerInformationVerifier
-dontwarn org.spongycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder
-dontwarn org.spongycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
-dontwarn org.spongycastle.cms.jcajce.JceKeyTransRecipient
-dontwarn org.spongycastle.crypto.BlockCipher
-dontwarn org.spongycastle.crypto.CipherParameters
-dontwarn org.spongycastle.crypto.engines.AESFastEngine
-dontwarn org.spongycastle.crypto.modes.CBCBlockCipher
-dontwarn org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher
-dontwarn org.spongycastle.crypto.params.KeyParameter
-dontwarn org.spongycastle.crypto.params.ParametersWithIV
-dontwarn org.spongycastle.jcajce.provider.digest.GOST3411$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.MD2$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.MD5$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.RIPEMD128$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.RIPEMD160$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.RIPEMD256$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.SHA1$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.SHA224$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.SHA256$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.SHA384$Digest
-dontwarn org.spongycastle.jcajce.provider.digest.SHA512$Digest
-dontwarn org.spongycastle.jce.X509Principal
-dontwarn org.spongycastle.jce.provider.BouncyCastleProvider
-dontwarn org.spongycastle.jce.provider.X509CertParser
-dontwarn org.spongycastle.ocsp.RevokedStatus
-dontwarn org.spongycastle.operator.ContentVerifierProvider
-dontwarn org.spongycastle.operator.DigestCalculator
-dontwarn org.spongycastle.operator.DigestCalculatorProvider
-dontwarn org.spongycastle.operator.OperatorCreationException
-dontwarn org.spongycastle.operator.bc.BcDigestCalculatorProvider
-dontwarn org.spongycastle.operator.jcajce.JcaContentVerifierProviderBuilder
-dontwarn org.spongycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
-dontwarn org.spongycastle.tsp.TimeStampRequest
-dontwarn org.spongycastle.tsp.TimeStampRequestGenerator
-dontwarn org.spongycastle.tsp.TimeStampResponse
-dontwarn org.spongycastle.tsp.TimeStampToken
-dontwarn org.spongycastle.tsp.TimeStampTokenInfo
-dontwarn sun.reflect.Reflection


-dontwarn com.ibm.icu.text.Bidi
-dontwarn java.awt.Canvas
-dontwarn java.awt.Component
-dontwarn java.awt.MediaTracker
-dontwarn java.awt.print.PrinterGraphics
-dontwarn java.awt.print.PrinterJob
-dontwarn org.apache.fop.complexscripts.fonts.GlyphSubstitutionTable
-dontwarn org.apache.fop.complexscripts.util.CharScript
-dontwarn org.apache.fop.complexscripts.util.GlyphSequence
-dontwarn org.apache.fop.fonts.apps.TTFReader
-dontwarn org.apache.fop.fonts.truetype.FontFileReader
-dontwarn org.apache.fop.fonts.truetype.TTFFile
-dontwarn org.bouncycastle.asn1.ASN1Encodable
-dontwarn org.bouncycastle.asn1.ASN1EncodableVector
-dontwarn org.bouncycastle.asn1.ASN1Enumerated
-dontwarn org.bouncycastle.asn1.ASN1InputStream
-dontwarn org.bouncycastle.asn1.ASN1Integer
-dontwarn org.bouncycastle.asn1.ASN1Object
-dontwarn org.bouncycastle.asn1.ASN1ObjectIdentifier
-dontwarn org.bouncycastle.asn1.ASN1OctetString
-dontwarn org.bouncycastle.asn1.ASN1OutputStream
-dontwarn org.bouncycastle.asn1.ASN1Primitive
-dontwarn org.bouncycastle.asn1.ASN1Sequence
-dontwarn org.bouncycastle.asn1.ASN1Set
-dontwarn org.bouncycastle.asn1.ASN1String
-dontwarn org.bouncycastle.asn1.ASN1TaggedObject
-dontwarn org.bouncycastle.asn1.DERNull
-dontwarn org.bouncycastle.asn1.DEROctetString
-dontwarn org.bouncycastle.asn1.DERSequence
-dontwarn org.bouncycastle.asn1.DERSet
-dontwarn org.bouncycastle.asn1.DERTaggedObject
-dontwarn org.bouncycastle.asn1.DERUTCTime
-dontwarn org.bouncycastle.asn1.cmp.PKIFailureInfo
-dontwarn org.bouncycastle.asn1.cms.Attribute
-dontwarn org.bouncycastle.asn1.cms.AttributeTable
-dontwarn org.bouncycastle.asn1.cms.ContentInfo
-dontwarn org.bouncycastle.asn1.cms.EncryptedContentInfo
-dontwarn org.bouncycastle.asn1.cms.EnvelopedData
-dontwarn org.bouncycastle.asn1.cms.IssuerAndSerialNumber
-dontwarn org.bouncycastle.asn1.cms.KeyTransRecipientInfo
-dontwarn org.bouncycastle.asn1.cms.OriginatorInfo
-dontwarn org.bouncycastle.asn1.cms.RecipientIdentifier
-dontwarn org.bouncycastle.asn1.cms.RecipientInfo
-dontwarn org.bouncycastle.asn1.ocsp.BasicOCSPResponse
-dontwarn org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers
-dontwarn org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
-dontwarn org.bouncycastle.asn1.tsp.MessageImprint
-dontwarn org.bouncycastle.asn1.tsp.TSTInfo
-dontwarn org.bouncycastle.asn1.x500.X500Name
-dontwarn org.bouncycastle.asn1.x509.AlgorithmIdentifier
-dontwarn org.bouncycastle.asn1.x509.Extension
-dontwarn org.bouncycastle.asn1.x509.Extensions
-dontwarn org.bouncycastle.asn1.x509.ExtensionsGenerator
-dontwarn org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
-dontwarn org.bouncycastle.asn1.x509.TBSCertificate
-dontwarn org.bouncycastle.asn1.x509.X509ObjectIdentifiers
-dontwarn org.bouncycastle.cert.X509CertificateHolder
-dontwarn org.bouncycastle.cert.jcajce.JcaX509CertificateHolder
-dontwarn org.bouncycastle.cert.ocsp.BasicOCSPResp
-dontwarn org.bouncycastle.cert.ocsp.CertificateID
-dontwarn org.bouncycastle.cert.ocsp.CertificateStatus
-dontwarn org.bouncycastle.cert.ocsp.OCSPReq
-dontwarn org.bouncycastle.cert.ocsp.OCSPReqBuilder
-dontwarn org.bouncycastle.cert.ocsp.OCSPResp
-dontwarn org.bouncycastle.cert.ocsp.RevokedStatus
-dontwarn org.bouncycastle.cert.ocsp.SingleResp
-dontwarn org.bouncycastle.cms.CMSEnvelopedData
-dontwarn org.bouncycastle.cms.Recipient
-dontwarn org.bouncycastle.cms.RecipientId
-dontwarn org.bouncycastle.cms.RecipientInformation
-dontwarn org.bouncycastle.cms.RecipientInformationStore
-dontwarn org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
-dontwarn org.bouncycastle.cms.jcajce.JceKeyTransRecipient
-dontwarn org.bouncycastle.crypto.BlockCipher
-dontwarn org.bouncycastle.crypto.CipherParameters
-dontwarn org.bouncycastle.crypto.engines.AESEngine
-dontwarn org.bouncycastle.crypto.modes.CBCBlockCipher
-dontwarn org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher
-dontwarn org.bouncycastle.crypto.params.KeyParameter
-dontwarn org.bouncycastle.crypto.params.ParametersWithIV
-dontwarn org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory
-dontwarn org.bouncycastle.jce.provider.BouncyCastleProvider
-dontwarn org.bouncycastle.jce.provider.X509CRLParser
-dontwarn org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder
-dontwarn org.bouncycastle.operator.DigestCalculator
-dontwarn org.bouncycastle.operator.DigestCalculatorProvider
-dontwarn org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
-dontwarn org.bouncycastle.tsp.TimeStampRequest
-dontwarn org.bouncycastle.tsp.TimeStampRequestGenerator
-dontwarn org.bouncycastle.tsp.TimeStampResponse
-dontwarn org.bouncycastle.tsp.TimeStampToken
-dontwarn org.bouncycastle.tsp.TimeStampTokenInfo
