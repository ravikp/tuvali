package io.mosip.tuvali.opentelemetry;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

public class Configuration {

  private static Resource resource = Resource.getDefault()
    .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "tuvali")));
  private static OtlpGrpcSpanExporter jaegerOtlpExporter =
    OtlpGrpcSpanExporter.builder()
      .setEndpoint("http://192.168.0.217:4317")
      .setTimeout(30, TimeUnit.SECONDS)
      .build();
  private static SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
    .addSpanProcessor(BatchSpanProcessor.builder(jaegerOtlpExporter).build())
    .setResource(resource)
    .build();

 /* private static SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
    .registerMetricReader(PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder().build()).build())
    .setResource(resource)
    .build();
*/

 /* private static SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
    .addLogRecordProcessor(BatchLogRecordProcessor.builder(OtlpGrpcLogRecordExporter.builder().build()).build())
    .setResource(resource)
    .build();
*/

/*
  ManagedChannel jaegerChannel = ManagedChannelBuilder.forAddress("localhost", 3336)
    .usePlaintext()
    .build();*/

/*
  private static JaegerGrpcSpanExporter jaegerExporter = JaegerGrpcSpanExporter.builder().build();

  private static SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
    .addSpanProcessor(BatchSpanProcessor.builder(jaegerExporter).build())
    .setResource(resource)
    .build();
*/


  public static OpenTelemetry initOpenTelemetry() {

    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
      .setTracerProvider(sdkTracerProvider)
      .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
      .buildAndRegisterGlobal();

    return openTelemetry;
  }
}



