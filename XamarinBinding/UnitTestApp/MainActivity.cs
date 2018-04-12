using System.Reflection;
using Android.App;
using Android.OS;
using Xamarin.Android.NUnitLite;

namespace UnitTestApp
{
    [Activity(Label = "UnitTestApp", MainLauncher = true, Icon = "@drawable/icon")]
    public class MainActivity : TestSuiteActivity
    {
        public static MainActivity sInstance = null;

        protected override void OnCreate(Bundle bundle)
        {
            // tests can be inside the main assembly
            AddTest(Assembly.GetExecutingAssembly());
            // or in any reference assemblies
            // AddTest (typeof (Your.Library.TestClass).Assembly);

            MainActivity.sInstance = this;

            // Once you called base.OnCreate(), you cannot add more assemblies.
            base.OnCreate(bundle);
        }

        protected override void OnDestroy()
        {
            base.OnDestroy();

            if (Equals(sInstance))
            {
                sInstance = null;
            }
        }
    }
}

