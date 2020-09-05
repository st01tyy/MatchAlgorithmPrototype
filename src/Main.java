import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    static ExecutorService executorService = Executors.newFixedThreadPool(2);
    static ExecutorService adderThreadPool = Executors.newFixedThreadPool(10);
    public static void main(String[] args) throws InterruptedException
    {
        Memory.initialize();
        Maintainer maintainer = new Maintainer();
        Matcher matcher = new Matcher();
        for(int i = 0; i < 6000; i++)
        {
            adderThreadPool.execute(new Adder(new User(new Random().nextInt(3000))));
        }
        System.out.println("6000个用户");
        executorService.execute(maintainer);
        executorService.execute(matcher);
        for(int k =  0; k < 9; k++)
        {
            for(int i = 0; i < 6000; i++)
            {
                adderThreadPool.execute(new Adder(new User(new Random().nextInt(3000))));
            }
            Thread.sleep(5000);
        }
        while(Memory.matched < Memory.target)
        {
            Thread.sleep(1000);
            System.out.println("Main thread awake");
        }
        System.out.println("共" + Memory.result.size() + "个匹配结果");
        for(int i = 0; i < Memory.result.size(); i++)
        {
            List<User> list = Memory.result.get(i);
            list.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    if(o1.score < o2.score)
                        return -1;
                    else if(o1.score == o2.score)
                        return 0;
                    else
                        return 1;
                }
            });
            if(list.size() == 0)
                System.out.println("nani?");
            System.out.println("平均等待时间：" + avg(list) + "，分数方差：" + fangcha(list) + "，最低分：" + list.get(0).score + "，最高分" + list.get(5).score);
        }
        executorService.shutdownNow();
        adderThreadPool.shutdownNow();
    }

    private static double fangcha(List<User> list)
    {
        int sumScore = 0;
        for(User user : list)
        {
            sumScore += user.score;
        }
        double avgScore = (double) sumScore / (double) list.size();
        double sumPowDif = 0;
        for(User user : list)
        {
            sumPowDif += Math.pow(user.score - avgScore, 2);
        }
        return sumPowDif / list.size();
    }

    private static double avg(List<User> list)
    {
        int sumT = 0;
        for(User user : list)
        {
            sumT += user.t;
        }
        return (double) sumT / (double) list.size();
    }

    public static void addBack(List<User> list)
    {
        for(User user : list)
        {
            adderThreadPool.execute(new Adder(user));
        }
    }

}
