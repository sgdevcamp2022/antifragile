package net.devh.boot.grpc.examples.lib;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: membership.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MembershipServiceGrpc {

  private MembershipServiceGrpc() {}

  public static final String SERVICE_NAME = "MembershipService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<net.devh.boot.grpc.examples.lib.Post,
      net.devh.boot.grpc.examples.lib.Account> getGetAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAccount",
      requestType = net.devh.boot.grpc.examples.lib.Post.class,
      responseType = net.devh.boot.grpc.examples.lib.Account.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<net.devh.boot.grpc.examples.lib.Post,
      net.devh.boot.grpc.examples.lib.Account> getGetAccountMethod() {
    io.grpc.MethodDescriptor<net.devh.boot.grpc.examples.lib.Post, net.devh.boot.grpc.examples.lib.Account> getGetAccountMethod;
    if ((getGetAccountMethod = MembershipServiceGrpc.getGetAccountMethod) == null) {
      synchronized (MembershipServiceGrpc.class) {
        if ((getGetAccountMethod = MembershipServiceGrpc.getGetAccountMethod) == null) {
          MembershipServiceGrpc.getGetAccountMethod = getGetAccountMethod =
              io.grpc.MethodDescriptor.<net.devh.boot.grpc.examples.lib.Post, net.devh.boot.grpc.examples.lib.Account>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  net.devh.boot.grpc.examples.lib.Post.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  net.devh.boot.grpc.examples.lib.Account.getDefaultInstance()))
              .setSchemaDescriptor(new MembershipServiceMethodDescriptorSupplier("getAccount"))
              .build();
        }
      }
    }
    return getGetAccountMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MembershipServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MembershipServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MembershipServiceStub>() {
        @java.lang.Override
        public MembershipServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MembershipServiceStub(channel, callOptions);
        }
      };
    return MembershipServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MembershipServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MembershipServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MembershipServiceBlockingStub>() {
        @java.lang.Override
        public MembershipServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MembershipServiceBlockingStub(channel, callOptions);
        }
      };
    return MembershipServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MembershipServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MembershipServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MembershipServiceFutureStub>() {
        @java.lang.Override
        public MembershipServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MembershipServiceFutureStub(channel, callOptions);
        }
      };
    return MembershipServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class MembershipServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAccount(net.devh.boot.grpc.examples.lib.Post request,
        io.grpc.stub.StreamObserver<net.devh.boot.grpc.examples.lib.Account> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAccountMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAccountMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                net.devh.boot.grpc.examples.lib.Post,
                net.devh.boot.grpc.examples.lib.Account>(
                  this, METHODID_GET_ACCOUNT)))
          .build();
    }
  }

  /**
   */
  public static final class MembershipServiceStub extends io.grpc.stub.AbstractAsyncStub<MembershipServiceStub> {
    private MembershipServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MembershipServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MembershipServiceStub(channel, callOptions);
    }

    /**
     */
    public void getAccount(net.devh.boot.grpc.examples.lib.Post request,
        io.grpc.stub.StreamObserver<net.devh.boot.grpc.examples.lib.Account> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAccountMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MembershipServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<MembershipServiceBlockingStub> {
    private MembershipServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MembershipServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MembershipServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public net.devh.boot.grpc.examples.lib.Account getAccount(net.devh.boot.grpc.examples.lib.Post request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAccountMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MembershipServiceFutureStub extends io.grpc.stub.AbstractFutureStub<MembershipServiceFutureStub> {
    private MembershipServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MembershipServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MembershipServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<net.devh.boot.grpc.examples.lib.Account> getAccount(
        net.devh.boot.grpc.examples.lib.Post request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAccountMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ACCOUNT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MembershipServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MembershipServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ACCOUNT:
          serviceImpl.getAccount((net.devh.boot.grpc.examples.lib.Post) request,
              (io.grpc.stub.StreamObserver<net.devh.boot.grpc.examples.lib.Account>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MembershipServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MembershipServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return net.devh.boot.grpc.examples.lib.MembershipProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MembershipService");
    }
  }

  private static final class MembershipServiceFileDescriptorSupplier
      extends MembershipServiceBaseDescriptorSupplier {
    MembershipServiceFileDescriptorSupplier() {}
  }

  private static final class MembershipServiceMethodDescriptorSupplier
      extends MembershipServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MembershipServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MembershipServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MembershipServiceFileDescriptorSupplier())
              .addMethod(getGetAccountMethod())
              .build();
        }
      }
    }
    return result;
  }
}
