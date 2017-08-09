# AWSJava
1. Prerequisites
- AWS Account, AWS IAM Credentials (containting secret key id and secret key): see tutorial of AWS
- AWS SDK for Java (include aws libs and third party libs in the projects): download from AWS website
- S3 bucket: creating a S3 bucket and set its name <br/>
Note: Remember regions of each amazon services <br/>
Optional:
- Java Swing to initialize User Interface
- Java CV to use camera and implete some facial detection algorithms (such as Haar Cascade)

2. Projects in this reposity

- CheckInByCamera: authenticating users by picture captured by webcam
- CompareFaceDemoV2: capture image from webcam and compare with an image stored in S3 bucket
- CompareFacesDemo: compare faces between a source image and a set of target images
- DetectFaceDemo: detect faces in an image

3. Important note:
* To create Basic AWS Credentials
  
  <code>BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyID, secretAccessKey);</code>
  
* To use any services of amazon, you should create client

  <code>AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()<br/>
                .withRegion(region)<br/>
                .withCredentials(new AWSStaticCredentialsProvider(credentials))<br/>
                .build();<br/>
  </code>
* Pair request/result
  <code>
  //make request<br/>
  DetectFacesRequest detectFacesRequest = new DetectFacesRequest()<br/>
      .withImage(new Image()<br/>
          .withS3Object(new S3Object()<br/>
              .withBucket(bucketName)<br/>
              .withName(key)))<br/>
      .withAttributes(Attribute.ALL);<br/>
  //get result<br/>
  DetectFacesResult result = rekognitionClient.detectFaces(detectFacesRequest);
  </code>
  
4. AWS Rekognition
These projects mainly use AWS Rekognition to analyze images and get results. Images can be tranfered to Rekognition by 2 ways:
- Use ByteBuffer
- Use S3Object
  <code>
  Image image = new Image().withS3Object(new S3Object()
                                            .withBucket(bucketName)
                                            .withName(key)));
  </code>
