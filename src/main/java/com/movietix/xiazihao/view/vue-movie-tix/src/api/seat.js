// // 获取场次座位信息
// import router from "@/router";
//
// router.get('/works/seats', async (req, res) => {
//     try {
//         const { id } = req.query;
//
//         if (!id) {
//             return res.json({ code: 0, message: '场次ID不能为空' });
//         }
//
//         // 1. 获取场次信息
//         const screening = await Screening.findByPk(id, {
//             include: [
//                 {
//                     model: Hall,
//                     attributes: ['rows', 'cols', 'seatMap']
//                 },
//                 {
//                     model: Movie,
//                     attributes: ['title']
//                 }
//             ]
//         });
//
//         if (!screening) {
//             return res.json({ code: 0, message: '场次不存在' });
//         }
//
//         // 2. 获取已售和预占座位
//         const orders = await Order.findAll({
//             where: {
//                 screeningId: id,
//                 status: { [Op.in]: [1, 2] } // 1: 已支付, 2: 待支付
//             }
//         });
//
//         const occupiedSeats = new Set();
//         orders.forEach(order => {
//             JSON.parse(order.seats).forEach(seat => occupiedSeats.add(seat));
//         });
//
//         // 3. 生成座位信息
//         const seats = [];
//         const { rows, cols, seatMap } = screening.Hall;
//         const seatPrice = screening.price; // 场次基础价格
//
//         // 如果有自定义座位图，使用座位图配置
//         const customSeatMap = seatMap ? JSON.parse(seatMap) : null;
//
//         for (let row = 1; row <= rows; row++) {
//             for (let col = 1; col <= cols; col++) {
//                 const seatNumber = (row - 1) * cols + col;
//                 const seatNo = `${String.fromCharCode(64 + row)}${col.toString().padStart(2, '0')}`;
//
//                 // 计算座位价格（如果有自定义价格）
//                 let price = seatPrice;
//                 if (customSeatMap && customSeatMap[row] && customSeatMap[row][col]) {
//                     price = customSeatMap[row][col].price || seatPrice;
//                 }
//
//                 seats.push({
//                     seatRow: row,
//                     seatCol: col,
//                     seatNo,
//                     price,
//                     status: occupiedSeats.has(seatNumber) ? 'occupied' : 'available'
//                 });
//             }
//         }
//
//         res.json({
//             code: 1,
//             msg: '操作成功',
//             data: {
//                 movieTitle: screening.Movie.title,
//                 screeningTime: screening.startTime,
//                 hallName: screening.Hall.name,
//                 seats
//             }
//         });
//     } catch (error) {
//         console.error(error);
//         res.status(500).json({ code: 0, message: '获取座位信息失败' });
//     }
// });