<template>
  <canvas ref="canvas"></canvas>
</template>

<script>
export default {
  mounted() {
    this.init();
    this.animate();
  },
  methods: {
    init() {
      this.canvas = this.$refs.canvas;
      this.ctx = this.canvas.getContext('2d');

      this.canvas.width = window.innerWidth;
      this.canvas.height = window.innerHeight;

      this.particles = [];
      this.numParticles = 100;

      for (let i = 0; i < this.numParticles; i++) {
        this.particles.push({
          x: Math.random() * this.canvas.width,
          y: Math.random() * this.canvas.height,
          vx: Math.random() * 2 - 1,
          vy: Math.random() * 2 - 1,
          size: Math.random() * 2 + 1,
          color: '#fff'
        });
      }
    },
    draw() {
      this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

      for (let i = 0; i < this.particles.length; i++) {
        const p = this.particles[i];

        this.ctx.beginPath();
        this.ctx.fillStyle = p.color;
        this.ctx.arc(p.x, p.y, p.size, 0, Math.PI * 2);
        this.ctx.fill();

        for (let j = i + 1; j < this.particles.length; j++) {
          const p2 = this.particles[j];
          this.drawLine(p, p2);
        }
      }
    },
    drawLine(p1, p2) {
      const dx = p2.x - p1.x;
      const dy = p2.y - p1.y;
      const dist = Math.sqrt(dx * dx + dy * dy);

      if (dist < 100) {
        this.ctx.beginPath();
        this.ctx.strokeStyle = '#fff';
        this.ctx.lineWidth = 1 - dist / 100;
        this.ctx.moveTo(p1.x, p1.y);
        this.ctx.lineTo(p2.x, p2.y);
        this.ctx.stroke();
      }
    },
    update() {
      for (let i = 0; i < this.particles.length; i++) {
        const p = this.particles[i];

        p.x += p.vx;
        p.y += p.vy;

        if (p.x < 0 || p.x > this.canvas.width) {
          p.vx = -p.vx;
        }

        if (p.y < 0 || p.y > this.canvas.height) {
          p.vy = -p.vy;
        }
      }
    },
    animate() {
      this.draw();
      this.update();
      requestAnimationFrame(this.animate);
    }
  },
  beforeUnmount() {
    cancelAnimationFrame(this.animate);
  }
};
</script>

<style scoped>
canvas {
  position: fixed;
  z-index: -1;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  display: block;
  background-color: #4083E0;
}
</style>
