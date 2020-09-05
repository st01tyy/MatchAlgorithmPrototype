public class Adder implements Runnable
{
    private User user;

    public Adder(User user)
    {
        this.user = user;
    }

    @Override
    public void run()
    {
        Memory.elements[user.score / 10].addUser(user);
    }
}
