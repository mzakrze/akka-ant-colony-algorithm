var COLOR = {
    ANT: {r: 0, g: 0, b: 0},
    ANTHILL: {r: 0, g: 255, b: 0},
    FOOD: {r: 255, g: 0, b: 0},
    PHEROMONE: {r: 120, g: 120, b: 120},
}

var CANVAS_WIDTH = 200;
var CANVAS_HEIGHT = 200;

var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");
var canvasData = ctx.getImageData(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

function run(){
    console.log('refreshing')
    doRefresh();
    setTimeout(500, run)
}

run()

function doRefresh() {

    var data = apiMock();

    for(var i = 0; i < CANVAS_HEIGHT * CANVAS_WIDTH * 4; i++) {
        canvasData.data[i] = 0;
    }

    for(let ant of data.ants) {
        drawPixel(ant.x, ant.y, COLOR.ANT, 255);
    }

    for(let p of data.pheromones) {
        drawPixel(p.x, p.y, COLOR.PHEROMONE, 255);
    }

    for(let f of data.food) {
        drawPixel(f.x, f.y, COLOR.FOOD, 255);
    }

    ctx.putImageData(canvasData, 0, 0);

    function drawPixel (x, y, c, alpha) {
        var index = (x + y * CANVAS_WIDTH) * 4;

        canvasData.data[index + 0] = c.r;
        canvasData.data[index + 1] = c.g;
        canvasData.data[index + 2] = c.b;
        canvasData.data[index + 3] = alpha;
    }
}


function apiMock() {

    var ants = [
        {x: 150, y: 150},
        {x: 150, y: 150},
        {x: 200, y: 200},
    ];

    var pheromones = [
        {x: 50, y: 50},
        {x: 50, y: 51},
        {x: 50, y: 52},
        {x: 50, y: 53},
        {x: 50, y: 54},
    ]

    return {
        ants: ants,
        anthill: {x: 100, y: 100},
        food: [],
        pheromones: pheromones
    }
}
