package uz.gita.myapplication


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import uz.gita.myapplication.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerFragment) as NavHostFragment

        binding.bottomNav.setupWithNavController(navHostFragment.navController)



        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, _ ->
            // invisible some fragments later
            when (destination.id) {

                R.id.mainFragment, R.id.cardsFragment, R.id.transferFragment, R.id.historyFragment, R.id.profileFragment -> {
                    lifecycleScope.launchWhenCreated {
                        binding.bottomNav.animate().apply {
                            duration = 400L
                            alpha(1f)
                            interpolator = AnticipateOvershootInterpolator()
                            translationY(0f)
                            start()
                        }
                        delay(400L)
                        binding.bottomNav.isVisible = true
                    }

                }

                else -> {
                    lifecycleScope.launchWhenCreated {
                        binding.bottomNav.animate().apply {
                            duration = 400L
                            interpolator = AnticipateOvershootInterpolator()
                            alpha(0f)
                            translationY(200f)
                            start()

                        }
                        delay(400L)
                        binding.bottomNav.isVisible = false
                    }
                }
            }

        }

    }

}