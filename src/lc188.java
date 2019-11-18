/**
 * @author qhhu
 * @date 2019/11/16 - 9:54
 *
 * [188] 买卖股票的最佳时机 IV
 *
 * 题目: 给定数组中第i个元素时一支给定的股票在第i天的价格, 最多可以完成两k笔交易, 返回可以获得的最大利润
 *      (不能同时参与多笔交易（必须在再次购买前出售掉之前的股票）)
 *
 * 难度: hard
 *
 * 思路: 动态规划
 */
class Solution {
    public int maxProfit(int k, int[] prices) {
        if (prices.length <= 1 || k < 1) {
            return 0;
        }

        // 当k大于等于数组长度一半时, 有k次交易, 每次交易包含买入卖出两种操作,
        // 又不能同时参与多笔交易（必须在再次购买前出售掉之前的股票）, 所以问题
        // 退化为不限交易次数, 使用买卖股票的最佳时机 II 的方法
        if (k >= prices.length >> 1) {
            int n = prices.length;
            int dp_i_0 = 0, dp_i_1 = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int temp = dp_i_0;
                dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
                dp_i_1 = Math.max(dp_i_1, temp - prices[i]);
            }
            return dp_i_0;
        }

        int max_k = k; // 最大交易次数
        int len = prices.length;
        // dp[i][k][0], 第i天, 交易k次, 不持有股票可以过得的最大利润
        // dp[i][k][1], 第i天, 交易k次, 持有股票可以过得的最大利润
        int[][][] dp = new int[len][max_k + 1][2];
        // dp[i][0][0] = 0, dp[i][0][1] = 0; // 不管哪天, 不交易就没有利润
        for (k = 1; k <= max_k; k++) {
            // dp[0][k][0], 第1天之前不可能持有股票, 即第1天没有股票可以买钱, 所以不管交易几次, 第1天不持有股票可以获得的最大利润为0
            // dp[0][k][1], 第1天持有股票可以获得的最大利润就是在第一天买下股票, 即-prices[0]
            dp[0][k][0] = 0;
            dp[0][k][1] = -prices[0];
        }

        for (int i = 1; i < len; i++) {
            for (k = 1; k <= max_k; k++) {
                // 今天不持有股票可以获得的最大利润 = max(昨天不持有股票并且今天不交易可以获得的最大利润, 昨天持有股票并且今天卖出股票可以获得的最大利润)
                // 买入卖出两个操作算一次交易               第i天前买入股票, 第i天卖出股票, 卖出的操作算第k次交易
                dp[i][k][0] = Math.max(dp[i - 1][k][0], dp[i - 1][k][1] + prices[i]);
                // 今天持有股票可以获得的最大利润 = max(昨天持有股票并且今天不交易可以获得的最大利润, 昨天不持有股票并且今天买入股票可以获得的最大利润)
                dp[i][k][1] = Math.max(dp[i - 1][k][1], dp[i - 1][k - 1][0] - prices[i]);
            }
        }

        return dp[len - 1][max_k][0];
    }
}
