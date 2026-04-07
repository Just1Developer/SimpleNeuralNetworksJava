package net.justonedev.ui;

import lombok.Getter;
import net.justonedev.neural.NeuralNetwork;
import net.justonedev.neural.Neuron;
import net.justonedev.neural.NeuronLayer;
import net.justonedev.ui.drawable.Circle;
import net.justonedev.ui.drawable.Drawable;
import net.justonedev.ui.drawable.Line;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Canvas extends JPanel {
    public static final int NEURON_SIZE = 30;
    private static final int LINE_WIDTH = 1;

    private static final int NEURON_SPACING = (int) (NEURON_SIZE * 0.3);
    private static final int LAYER_SPACING = NEURON_SIZE * 10;

    private static final int X_OFFSET = 10;
    private static final int Y_OFFSET = 200;
    private static final double MIN_ZOOM_LEVEL = 0.5;
    private static final double MAX_ZOOM_LEVEL = 5;
    private static final double ZOOM_STEP = 0.05;

    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color NEURON_BACKGROUND_COLOR = Color.WHITE;
    private static final Color NEURON_BORDER_COLOR = Color.BLACK;
    private static final Color NEURON_CONNECTION_COLOR = Color.darkGray;

    private int neuronMaxLayerHeight;

    private final Window window;
    private final List<Drawable> drawables;

    private final Map<Line, Float> brightness = new HashMap<>();

    @Getter
    private int cameraOffsetX;
    @Getter
    private int cameraOffsetY;
    @Getter
    private double zoomLevel;

    public Canvas(Window window) {
        this.window = window;
        super.setSize(window.getSize());
        cameraOffsetX = X_OFFSET;
        cameraOffsetY = Y_OFFSET;
        zoomLevel = 1.0;
        this.drawables = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics = (Graphics2D) g;

        int neuronSize = (int) Math.round(NEURON_SIZE * zoomLevel);
        int neuronBorderWidth = (int) Math.round(NEURON_SIZE * zoomLevel * 0.07);
        int lineWidth = (int) Math.round(LINE_WIDTH * zoomLevel);
        int neuronCenterOffset = neuronSize/2;

        graphics.setBackground(BACKGROUND_COLOR);

        for (Drawable drawable : drawables) {
            switch (drawable.getShape()) {
                case CIRCLE -> {
                    graphics.setStroke(new BasicStroke(neuronBorderWidth));
                    graphics.setColor(NEURON_BACKGROUND_COLOR);
                    graphics.fillOval(drawable.getCanvasX(this), drawable.getCanvasY(this), neuronSize, neuronSize);
                    graphics.setColor(NEURON_BORDER_COLOR);
                    graphics.drawOval(drawable.getCanvasX(this), drawable.getCanvasY(this), neuronSize, neuronSize);
                }
                case LINE -> {
                    Line line = (Line) drawable;
                    graphics.setStroke(new BasicStroke(lineWidth));

                    //region brightness
                    float brightness;
                    if (this.brightness.containsKey(line)) {
                        brightness = this.brightness.get(line);
                    } else {
                        brightness = (float) (Math.random() * 0.6 + 0.2);
                        this.brightness.put(line, brightness);
                    }
                    //endregion

                    graphics.setColor(Color.getHSBColor(1, 0, brightness));
                    graphics.drawLine(line.getCanvasSourceX(this) + neuronCenterOffset, line.getCanvasSourceY(this) + neuronCenterOffset,
                            line.getCanvasTargetX(this) + neuronCenterOffset, line.getCanvasTargetY(this) + neuronCenterOffset);
                }
            }
        }
    }

    private static class Reference<T> {
        private T value;
        public Reference(T value) {
            this.value = value;
        }
        public T get() {
            return value;
        }
        public void set(T value) {
            this.value = value;
        }
    }

    public void updateNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.drawables.clear();

        int maxHiddenLayerSize = Collections.max(neuralNetwork.getHiddenLayers(), Comparator.comparingInt(NeuronLayer::getSize)).getSize();
        this.neuronMaxLayerHeight = Math.max(Math.max(neuralNetwork.getInputLayer().getSize(), neuralNetwork.getOutputLayer().getSize()), maxHiddenLayerSize);

        Map<Neuron, Circle> neurons = new HashMap<>();
        Reference<Integer> x = new Reference<>(0);
        addLayer(neurons, neuralNetwork.getInputLayer(), null, x);

        NeuronLayer previousLayer = neuralNetwork.getInputLayer();
        for (NeuronLayer layer : neuralNetwork.getHiddenLayers()) {
            addLayer(neurons, layer, previousLayer, x);
            previousLayer = layer;
        }
        addLayer(neurons, neuralNetwork.getOutputLayer(), previousLayer, x);
        // Add neurons last
        this.drawables.addAll(neurons.values());
    }

    private void addLayer(Map<Neuron, Circle> neurons, NeuronLayer layer, NeuronLayer previousLayer, Reference<Integer> x) {
        // Create drawables for Neurons and add neurons to map
        int y = getLayerYPadding(layer);
        for (Neuron neuron : layer.getNeurons()) {
            Circle neuronCircle = new Circle(x.get(), y);
            y += NEURON_SPACING + NEURON_SIZE;
            neurons.put(neuron, neuronCircle);
        }
        x.set(x.get() + LAYER_SPACING);
        if (previousLayer == null) return;
        // Add connections directly to drawables list
        for (Neuron neuron : layer.getNeurons()) {
            // todo this loop structure enables us to directly reference the weight later
            Circle targetNeuron = neurons.get(neuron);
            for (Neuron source : previousLayer.getNeurons()) {
                Circle sourceNeuron = neurons.get(source);
                this.drawables.add(new Line(sourceNeuron, targetNeuron));
            }
        }
    }

    private int getLayerYPadding(NeuronLayer layer) {
        int totalLayerHeight = NEURON_SIZE * layer.getSize() + NEURON_SPACING * (layer.getSize() - 1);
        return (neuronMaxLayerHeight - totalLayerHeight) / 2;
    }

    private NeuralNetwork getNeuralNetwork() {
        return this.window.getNeuralNetwork();
    }

    //region Mouse Events

    public void zoomOut() {
        double oldZoomLevel = zoomLevel;
        zoomLevel = Math.max(MIN_ZOOM_LEVEL, zoomLevel - ZOOM_STEP);
        double relativeUpdate = oldZoomLevel - zoomLevel;
        updateCameraPosition((int) (relativeUpdate * getWidth() / 2), (int) (relativeUpdate * getHeight() / 2));
    }

    public void zoomIn() {
        double oldZoomLevel = zoomLevel;
        zoomLevel = Math.min(MAX_ZOOM_LEVEL, zoomLevel + ZOOM_STEP);
        double relativeUpdate = oldZoomLevel - zoomLevel;
        updateCameraPosition((int) (relativeUpdate * getWidth() / 2), (int) (relativeUpdate * getHeight() / 2));
    }

    public void updateCameraPosition(int deltaX, int deltaY) {
        updateCameraPosition(deltaX, deltaY, true);
    }
    public void updateCameraPosition(int deltaX, int deltaY, boolean useScale) {
        double scale = useScale ? zoomLevel : 1;
        this.cameraOffsetX += (int) (deltaX / scale);
        this.cameraOffsetY += (int) (deltaY / scale);
    }
}
